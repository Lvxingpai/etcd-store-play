package com.lvxingpai.etcd

import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}

/**
 * Created by zephyre on 11/1/15.
 */
class EtcdStoreModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    Seq(
      bind[Configuration] qualifiedWith "etcdConf" toProvider classOf[EtcdConfProvider],
      bind[Configuration] qualifiedWith "etcdService" toProvider classOf[EtcdServiceProvider]
    )
  }
}
