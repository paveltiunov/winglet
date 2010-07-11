package com.primalrecode.winglet

trait Injection {
  InjectorHolder.injector.injectMembers(this)
}