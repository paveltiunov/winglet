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
  private val uriFieldId = "uriField"
  private var contentFieldId = "contentField"
  private var templateListId = "templateList"

  def dispatch = {
    case "createForm" => createForm _
    case "templates" => templates _
  }

  def createForm(in:NodeSeq) = bind(
    "f", in,
    "uri" -%> SHtml.text(uri, uri = _, "id" -> uriFieldId),
    "content" -%> SHtml.textarea(content, content = _, "id" -> contentFieldId),
    "submit" -%> SHtml.submit("Save", createTemplate)
  )

  def templates(in:NodeSeq):NodeSeq = <div id={templateListId}>{templatesContent(in)}</div>

  def templatesContent(in:NodeSeq):NodeSeq = Template.allTemplates.flatMap(t => bind(
    "t", in,
    "edit" -> SHtml.a(<div>{t.uri}</div>) {
      val template = Template.reload(t)
      JsCmds.SetValById(uriFieldId, JE.strToS(template.uri)) & JsCmds.SetValById(contentFieldId, JE.strToS(template.content))
    },
    "remove" -> SHtml.a(<div>Remove</div>) {
      Template.remove(t)
      JsCmds.SetHtml(templateListId, templatesContent(in))
    }
  ))

  def createTemplate() = Template.create(uri, content)
}