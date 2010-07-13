package com.primalrecode.winglet

import com.google.inject.Inject

trait ModelAccess extends Injection {
  @Inject
  implicit var model:Model = _
}