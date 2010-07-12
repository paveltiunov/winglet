package com.primalrecode.winglet.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import com.primalrecode.winglet.model._
import com.primalrecode.winglet._
import com.google.inject.Inject

class HelloWorld extends Injection {
  @Inject
  var model:Model = _

  def howdy(in: NodeSeq): NodeSeq = {
    val page = new Page
    page.name = "foobar"
    model.persist(page)
    Helpers.bind("b", in,
      "time" -> model.find(classOf[Page], "foobar").map(_.name).getOrElse("oops"))
  }
}

