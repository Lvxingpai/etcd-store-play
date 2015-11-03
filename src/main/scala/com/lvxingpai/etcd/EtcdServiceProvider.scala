package com.lvxingpai.etcd

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Created by zephyre on 11/2/15.
 */
@Singleton
class EtcdServiceProvider @Inject()(config: Configuration, lifecycle: ApplicationLifecycle)
  extends EtcdBaseProvider {

  lazy val get: Configuration = {
    val (host, port, schema, auth) = getEtcdParams(config)
    val builder = new EtcdServiceBuilder(host, port, schema, auth)
    val confFuture = etcdConf(builder, config, "etcdStore.serviceKeys")
    Await.result(confFuture, Duration.Inf)
  }
}
