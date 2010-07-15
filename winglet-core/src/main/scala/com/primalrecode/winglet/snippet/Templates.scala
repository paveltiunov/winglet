package com.primalrecode.winglet.snippet

import com.primalrecode.winglet.{ModelAccess}
import xml.NodeSeq
import net.liftweb.http.{SHtml, StatefulSnippet}
import com.primalrecode.winglet.model.Template
import net.liftweb.util.BindHelpers
import net.liftweb.http.js.{JE, JsCmds}

class Templates extends StatefulSnippet with ModelAccess with BindHelpers {
  private var uri:String = ""
  private var content:String = ""

  def dispatch = {
    case "createForm" => createForm _
    case "templates" => templates _
  }

  def createForm(in:NodeSeq) = bind(
    "f", in,
    "uri" -%> SHtml.text(uri, uri = _, "id" -> "uri"),
    "content" -%> SHtml.textarea(content, content = _, "id" -> "content"),
    "submit" -%> SHtml.submit("Save", createTemplate)
  )

  def templates(in:NodeSeq):NodeSeq = Template.allTemplates.flatMap(t => bind("t", in, "editLink" -> SHtml.a(<div>{t.uri}</div>) {
    val template = Template.reload(t)
    JsCmds.SetValById("uri", JE.strToS(template.uri)) & JsCmds.SetValById("content", JE.strToS(template.content))
  }))

  def createTemplate() = Template.create(uri, content)
}