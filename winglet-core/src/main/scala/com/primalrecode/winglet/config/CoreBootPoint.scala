package com.primalrecode.winglet.config

import com.primalrecode.winglet.BootPoint
import net.liftweb.sitemap.{Menu, Loc, SiteMap}
import com.google.inject.Inject
import com.primalrecode.winglet.view.{Pages, TemplateView, ResourceView}
import net.liftweb.http.{ResourceServer, LiftRules}

class CoreBootPoint @Inject() (siteMapGen:SiteMapGen,
                               resourceView: ResourceView,
                               templateView:TemplateView,
                               pages:Pages) extends BootPoint {
  def boot() {
    // where to search snippet
    LiftRules.addToPackages("com.primalrecode.winglet")

    LiftRules.setSiteMapFunc(siteMapGen.generate)
    LiftRules.dispatch.prepend(resourceView.dispatch)
    LiftRules.viewDispatch.prepend(pages.dispatch)
    LiftRules.viewDispatch.prepend(templateView.dispatch)
    val oldAllowedPaths = ResourceServer.allowedPaths
    ResourceServer.allowedPaths = {
      case "jquery.blockUI.js" :: Nil => true
      case path => oldAllowedPaths(path)
    }
  }
}