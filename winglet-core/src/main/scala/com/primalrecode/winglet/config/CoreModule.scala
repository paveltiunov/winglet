package com.primalrecode.winglet.config

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import com.primalrecode.winglet.BootPoint

class CoreModule extends AbstractModule{
  def configure = {
    val bootPointBinder = Multibinder.newSetBinder(binder, classOf[BootPoint])
    bootPointBinder.addBinding().to(classOf[CoreBootPoint])
  }
}