package com.primalrecode.winglet.view

import net.liftweb.http.LiftView
import com.primalrecode.winglet.{Model, Injection}
import com.google.inject.Inject
import com.primalrecode.winglet.model.Page

class Pages extends LiftView with Injection{
  @Inject
  var model:Model = _;

  def dispatch = {
    case pageId => () => renderPage(pageId)
  }

  private def renderPage(pageId:String) = {
    val page = model.find(classOf[Page], pageId)
    <lift:surround with="default" at="content">
      <h3>{page.get.name}</h3>
    </lift:surround>
  }
}