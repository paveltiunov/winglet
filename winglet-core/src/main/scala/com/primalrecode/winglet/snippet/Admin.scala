package com.primalrecode.winglet.snippet

import net.liftweb.http.StatefulSnippet
import xml.NodeSeq
import net.liftweb.http.{S, SHtml, StatefulSnippet}
import net.liftweb.util.{Log, Helpers}
import Helpers._
import com.primalrecode.winglet.{Injection, Model}
import com.google.inject.Inject
import com.primalrecode.winglet.model.Page

class Admin extends StatefulSnippet with Injection {
  @Inject
  var model:Model = _

  private var pageName:String = _
  private var pageUrl:String = _
  
  def dispatch = {
    case "addPage" => addPage _
  }

  def addPage(in:NodeSeq) = bind("page", in,
    "name" -> SHtml.text("", pageName = _),
    "url" -> SHtml.text("", pageUrl = _),
    "submit" -> SHtml.submit("Create page", createPage))

  private def createPage() {
    val page = new Page
    page.name = pageName
    page.url = pageUrl
    model.persist(page)
  }
}