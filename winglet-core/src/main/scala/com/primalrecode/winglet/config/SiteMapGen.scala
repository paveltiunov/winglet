package com.primalrecode.winglet.config

import net.liftweb.sitemap.{Loc, Menu, SiteMap}
import com.google.inject.Inject
import com.primalrecode.winglet.Model
import com.primalrecode.winglet.model.Page

class SiteMapGen @Inject()(model: Model) {
  def generate(): SiteMap = {
    var entries = Menu(Loc("Home", List("index"), "Home")) ::
            Menu(Loc("Add page", List("admin", "addpage"), "Add page")) ::
            Menu(Loc("Add template", List("admin", "addtemplate"), "Add template")) ::
            Menu(Loc("Upload resource", List("admin", "uploadresource"), "Upload resource")) ::
            Nil
    val pages = model.findAll[Page]("allPages")
    entries ++= pages.map(p => Menu(Loc(p.name, List("pages", p.name), p.name)))
    SiteMap(entries: _*)
  }
}