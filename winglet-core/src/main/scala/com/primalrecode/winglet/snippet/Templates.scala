package com.primalrecode.winglet.snippet

import com.primalrecode.winglet.{ModelAccess}
import net.liftweb.http.{StatefulSnippet}
import com.primalrecode.winglet.model.Template
import net.liftweb.util.BindHelpers

class Templates extends StatefulSnippet with ModelAccess with BindHelpers with EntityEditor[Template] {
  private var uri:String = ""
  private var content:String = ""

  def saveEntity() = Template.create(uri, content)

  def entityFields = TextEntityField[Template]("uri", s => uri = s, e => e.uri) :: TextAreaEntityField[Template]("content", s => content = s, e => e.content) :: Nil

  def removeEntity(entity: Template) = Template.remove(entity)

  def entityListName(entity: Template) = entity.uri

  def allEntities = Template.allTemplates

  def dispatch = {
    case "templates" => editor _
  }
}