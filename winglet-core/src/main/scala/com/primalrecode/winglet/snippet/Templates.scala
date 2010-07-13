package com.primalrecode.winglet.snippet

import com.primalrecode.winglet.{ModelAccess}
import xml.NodeSeq
import net.liftweb.http.{SHtml, StatefulSnippet}
import net.liftweb.util.Helpers._
import com.primalrecode.winglet.model.Template

class Templates extends StatefulSnippet with ModelAccess {
  private var uri:String = _
  private var content:String = _

  def dispatch = {
    case "createForm" => createForm _
  }

  def createForm(in:NodeSeq) = bind(
    "f", in,
    "uri" -%> SHtml.text("", uri = _),
    "content" -%> SHtml.textarea("", content = _),
    "submit" -%> SHtml.submit("Save", createTemplate)
  )

  def createTemplate() = Template.create(uri, content)
}