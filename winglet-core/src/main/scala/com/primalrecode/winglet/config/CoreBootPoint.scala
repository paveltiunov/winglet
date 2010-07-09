package com.primalrecode.winglet.config

import com.primalrecode.winglet.BootPoint
import net.liftweb.sitemap.{Menu, Loc, SiteMap}
import net.liftweb.http.LiftRules
class CoreBootPoint extends BootPoint {
  def boot() {
    // where to search snippet
    LiftRules.addToPackages("com.primalrecode.winglet")

    // Build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) :: Nil
    LiftRules.setSiteMap(SiteMap(entries:_*))
  }
}