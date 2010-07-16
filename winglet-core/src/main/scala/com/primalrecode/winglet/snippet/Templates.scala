package com.primalrecode.winglet.snippet

import com.primalrecode.winglet.{ModelAccess}
import net.liftweb.http.{SHtml, StatefulSnippet}
import com.primalrecode.winglet.model.Template
import net.liftweb.util.BindHelpers
import net.liftweb.http.js.{JE, JsCmds}
import xml.{Elem, Text, NodeSeq}

class Templates extends StatefulSnippet with ModelAccess with BindHelpers {
  private var uri:String = ""
  private var content:String = ""
  private val uriFieldId = "uriField"
  private var contentFieldId = "contentField"
  private var templateListId = "templateList"

  def dispatch = {
    case "templates" => templates _
  }

  def createForm(templateListTemplate:NodeSeq)(in:NodeSeq) = SHtml.ajaxForm(bind(
    "f", in,
    "uri" -%> SHtml.text(uri, uri = _, "id" -> uriFieldId),
    "content" -%> SHtml.textarea(content, content = _, "id" -> contentFieldId),
    "submit" -%> SHtml.ajaxSubmit("Save", () => {
      createTemplate
      templateListRefreshCmd(templateListTemplate)
    })
  ))

  def templates(in:NodeSeq) = bind("t", in, "templateList" -%> templatesList _, "editForm" -%> createForm(chooseTemplate("t","templateList",in)) _)

  def templatesList(in:NodeSeq):Elem = <div id={templateListId}>{templatesContent(in)}</div>

  def templatesContent(in:NodeSeq):NodeSeq = Template.allTemplates.flatMap(t => bind(
    "t", in,
    "edit" -> SHtml.a(Text(t.uri)) {
      val template = Template.reload(t)
      JsCmds.SetValById(uriFieldId, JE.strToS(template.uri)) & JsCmds.SetValById(contentFieldId, JE.strToS(template.content))
    },
    "remove" -%> {linkText => SHtml.a(linkText) {
      Template.remove(t)
      templateListRefreshCmd(in)
    }}
  ))

  def templateListRefreshCmd(templateListTemplate:NodeSeq) = JsCmds.SetHtml(templateListId, templatesContent(templateListTemplate))

  def createTemplate() = Template.create(uri, content)
}