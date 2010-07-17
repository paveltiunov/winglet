package com.primalrecode.winglet.snippet

import net.liftweb.http.{StatefulSnippet}
import com.primalrecode.winglet.model.Page
import com.primalrecode.winglet.{ModelAccess}

class Pages extends StatefulSnippet with ModelAccess with EntityEditor[Page] {
  private var pageName:String = _
  private var pageUrl:String = _
  
  def dispatch = {
    case "editor" => editor _
  }

  def removeEntity(entity: Page) = Page.remove(entity:Page)

  def entityListName(entity: Page) = entity.url

  def allEntities = Page.allPages

  def saveEntity() = Page.create(pageName, pageUrl)

  def entityFields = TextEntityField[Page]("uri", s => pageUrl = s, e => e.url) :: TextEntityField[Page]("name", s => pageName = s, e => e.name) :: Nil
}