package com.primalrecode.winglet.snippet

import net.liftweb.http.{StatefulSnippet}
import com.primalrecode.winglet.model.Page
import com.primalrecode.winglet.{ModelAccess}

class Pages extends StatefulSnippet with ModelAccess with EntityEditor[Page] {
  private var pageName:String = _
  private var pageUrl:String = _
  private var includeInMenu:Boolean = _
  
  def dispatch = {
    case "editor" => editor _
  }

  def removeEntity(entity: Page) = Page.remove(entity:Page)

  def entityListName(entity: Page) = entity.url

  def allEntities = Page.allPages

  def saveEntity() = Page.create(pageName, pageUrl, includeInMenu)

  def entityFields = TextEntityField[Page]("uri", s => pageUrl = s, e => e.url) ::
          TextEntityField[Page]("name", s => pageName = s, e => e.name) ::
          CheckboxEntityField[Page]("includeInMenu", b => includeInMenu = b, e => e.includeInMenu) :: 
          Nil
}