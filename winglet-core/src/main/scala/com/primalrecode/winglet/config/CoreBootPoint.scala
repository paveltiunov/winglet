package com.primalrecode.winglet.config

import com.primalrecode.winglet.BootPoint
import net.liftweb.sitemap.{Menu, Loc, SiteMap}
import net.liftweb.http.LiftRules
import com.google.inject.Inject
import com.primalrecode.winglet.view.ResourceView

class CoreBootPoint @Inject() (siteMapGen:SiteMapGen, resourceView: ResourceView) extends BootPoint {
  def boot() {
    // where to search snippet
    LiftRules.addToPackages("com.primalrecode.winglet")

    LiftRules.setSiteMapFunc(siteMapGen.generate)
    LiftRules.dispatch.prepend(resourceView.dispatch)
  }
}