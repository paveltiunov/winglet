package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence.{NamedQuery, Id, Entity}
import java.util.List
import com.primalrecode.winglet.Model

@Entity
@NamedQuery(name = "allPages", query = "SELECT p FROM Page p")
class Page {
  @BeanProperty
  var name: String = _

  @BeanProperty
  @Id  
  var url: String = _

  @BeanProperty
  var blocks: List[Block] = _
}

object Page extends ModelSugar[Page] {
  def create(name:String, url:String)(implicit model:Model) = {
    val page = new Page
    page.name = name
    page.url = url
    model.inTransaction(() => model.persistAndFlush(page))
  }

  def entityId(e: Page) = e.url

  def entityClass = classOf[Page]

  def allPages(implicit model:Model) = all

  def allQueryName = "allPages"
}