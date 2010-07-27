package com.primalrecode.winglet.config

import net.liftweb.sitemap.{Loc, Menu, SiteMap}
import com.google.inject.Inject
import com.primalrecode.winglet.Model
import com.primalrecode.winglet.model.Page
import com.primalrecode.winglet.view.UriHelpers
import net.liftweb.sitemap.Loc._
import com.primalrecode.winglet.auth.isAdminUser
import net.liftweb.http.ForbiddenResponse

class SiteMapGen @Inject()(model: Model) extends UriHelpers {
  def generate(): SiteMap = {
    var entries = Menu(Loc("Home", List("index"), "Home")) ::
            Menu(Loc("Admin", List("admin", "index"), "Administration", If(() => isAdminUser, () => ForbiddenResponse())),
              Menu(Loc("Pages", List("admin", "pages"), "Pages", LocGroup("admin"), Hidden)),
              Menu(Loc("Templates", List("admin", "templates"), "Templates", LocGroup("admin"), Hidden)),
              Menu(Loc("Resources", List("admin", "resources"), "Resources", LocGroup("admin"), Hidden))
            ) ::
            Nil
    val pages = model.findAll[Page]("allPages")
    entries ++= pages.filter(p => !alreadyInEntries(splitUri(p.url
      ), entries)).filter(p => p.name != null && splitUri(p.url).length > 0).map(p => Menu(Loc(p.name, splitUri(p.url), p.name, if (p.includeInMenu) Nil else List(Loc.Hidden))))
    SiteMap(entries: _*)
  }

  private def alreadyInEntries(path:List[String], entries:List[Menu]):Boolean = entries match {
    case Nil => false
    case x :: xs => if(x.loc.link.uriList == path) true else alreadyInEntries(path, xs)
  }
}