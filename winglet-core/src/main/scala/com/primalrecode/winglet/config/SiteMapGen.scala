package com.primalrecode.winglet.config

import net.liftweb.sitemap.{Loc, Menu, SiteMap}
import com.google.inject.Inject
import com.primalrecode.winglet.Model
import com.primalrecode.winglet.model.Page
import com.primalrecode.winglet.view.UriHelpers

class SiteMapGen @Inject()(model: Model) extends UriHelpers {
  def generate(): SiteMap = {
    var entries = Menu(Loc("Home", List("index"), "Home")) ::
            Menu(Loc("Add page", List("admin", "addpage"), "Add page")) ::
            Menu(Loc("Add template", List("admin", "addtemplate"), "Add template")) ::
            Menu(Loc("Upload resource", List("admin", "uploadresource"), "Upload resource")) ::
            Nil
//    val pages = model.findAll[Page]("allPages")
//    entries ++= pages.filter(p => p.name != null && splitUri(p.url).length > 0).map(p => Menu(Loc(p.name, splitUri(p.url), p.name)))
    SiteMap(entries: _*)
  }
}