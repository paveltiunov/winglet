package com.primalrecode.winglet.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import com.primalrecode.winglet.model._
import com.primalrecode.winglet._

class HelloWorld {
  def howdy(in: NodeSeq): NodeSeq = {
    val page = new Page
    page.name = "foobar"
    page.text = "true"
    Model.persist(page)
    Helpers.bind("b", in,
      "time" -> Model.find(classOf[Page], "foobar").map(_.text).getOrElse("oops"))
  }
}

