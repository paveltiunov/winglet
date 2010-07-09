package com.primalrecode.winglet

import com.google.inject.Inject
import java.util.Set

class BootPointExecutor @Inject() (points: Set[BootPoint]) {
  def boot() = {
    points.toArray(new Array[BootPoint](0)).foreach(_.boot())
  }
}