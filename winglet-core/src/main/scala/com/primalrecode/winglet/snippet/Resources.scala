package com.primalrecode.winglet.snippet

import net.liftweb.http.{FileParamHolder, StatefulSnippet}
import com.primalrecode.winglet.model.Resource
import com.primalrecode.winglet.{ModelAccess}

class Resources extends StatefulSnippet with ModelAccess with EntityEditor[Resource] {
  private var file:FileParamHolder = _
  private var url:String = _

  def dispatch = {
    case "editor" => editor _
  }

  def removeEntity(entity: Resource) = Resource.remove(entity)

  def entityListName(entity: Resource) = entity.url

  def allEntities = Resource.all

  def saveEntity() = Resource.create(url, file.file, file.mimeType)

  def entityFields = TextEntityField[Resource]("uri", s => url = s, e => e.url) ::
          FileEntityField[Resource]("file", f => file = f) :: Nil

  private def commitFile() = Resource.create(url, file.file, file.mimeType)
}