package com.lvxingpai.etcd

import java.util.{ ArrayList => JArrayList }
import javax.inject.Provider
import com.typesafe.config.{ ConfigValueFactory, ConfigValueType }
import play.api.Configuration
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by zephyre on 11/2/15.
 */
abstract class EtcdBaseProvider extends Provider[Configuration] {
  /**
   * 获得etcd服务器的连接信息
   * @param configuration 本地Play配置
   * @return
   */
  protected def getEtcdParams(configuration: Configuration): (String, Int, String, Option[EtcdAuth]) = {
    val host = configuration getString "etcdStore.host" getOrElse "localhost"
    val port = configuration getInt "etcdStore.port" getOrElse 2379
    val schema = configuration getString "etcdStore.schema" getOrElse "http"
    val auth = for {
      user <- configuration getString "etcdStore.user"
      password <- configuration getString "etcdStore.password"
    } yield {
      EtcdAuth(user, password)
    }
    (host, port, schema, auth)
  }

  /**
   * 从etcd获得builder的信息
   * @param configuration 本地Play配置
   * @param builderKeys 有哪些key需要添加到builder中
   * @return
   */
  protected def etcdConf(builder: EtcdBaseBuilder, configuration: Configuration,
    builderKeys: String): Future[Configuration] = {
    // 获得所有的键
    val confKeys = (configuration getList builderKeys getOrElse ConfigValueFactory.fromIterable(new JArrayList())).toSeq

    // 从本地配置中获取keys，构建builder
    val finalBuilder = confKeys.foldLeft(builder)((builder, configValue) => {
      configValue.valueType() match {
        case ConfigValueType.STRING => builder.addKeys(configValue.unwrapped.toString)
        case ConfigValueType.OBJECT =>
          val key = configValue atKey "root" getString "root.key"
          val alias = configValue atKey "root" getString "root.alias"
          builder.addKeysWithAliases(key -> alias)
        case _ =>
          assert(assertion = false, s"Invalid ConfigValueType for $configValue")
          builder
      }
    })

    finalBuilder.build() map Configuration.apply
  }
}
