package com.primalrecode.winglet.config

import net.liftweb.sitemap.{Loc, Menu, SiteMap}
import com.google.inject.Inject
import com.primalrecode.winglet.Model
import com.primalrecode.winglet.model.Page
import com.primalrecode.winglet.view.UriHelpers
import net.liftweb.sitemap.Loc._

class SiteMapGen @Inject()(model: Model) extends UriHelpers {
  def generate(): SiteMap = {
    var entries = Menu(Loc("Home", List("index"), "Home")) ::
            Menu(Loc("Admin", List("admin", "index"), "Administration"),
              Menu(Loc("Pages", List("admin", "pages"), "Pages", LocGroup("admin"), Hidden)),
              Menu(Loc("Templates", List("admin", "templates"), "Templates", LocGroup("admin"), Hidden)),
              Menu(Loc("Resources", List("admin", "resources"), "Resources", LocGroup("admin"), Hidden))
            ) ::
            Nil
//    val pages = model.findAll[Page]("allPages")
//    entries ++= pages.filter(p => p.name != null && splitUri(p.url).length > 0).map(p => Menu(Loc(p.name, splitUri(p.url), p.name)))
    SiteMap(entries: _*)
  }
}