package com.primalrecode.winglet.gaeauth.config

import com.google.inject.{AbstractModule}
import com.google.inject.multibindings.Multibinder
import com.primalrecode.winglet.BootPoint

class GaeAuthModule extends AbstractModule {
  def configure = {
    val bootPointBinder = Multibinder.newSetBinder(binder, classOf[BootPoint])
    bootPointBinder.addBinding.to(classOf[GaeAuthBootPoint])
  }
}