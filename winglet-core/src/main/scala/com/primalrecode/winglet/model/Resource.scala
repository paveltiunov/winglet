package com.primalrecode.winglet.model

import reflect.BeanProperty
import com.primalrecode.winglet.Model
import javax.persistence.{NamedQuery, Lob, Id, Entity}

@Entity
@NamedQuery(name = "allResources", query = "SELECT r FROM Resource r")
class Resource {
  @BeanProperty
  @Id
  var url:String = _

  @BeanProperty
  @Lob
  var content:Array[Byte] = _

  @BeanProperty
  var mimeType:String = _
}

object Resource extends ModelSugar[Resource]{
  def create(url:String, content:Array[Byte], mimeType:String) (implicit model:Model) {
    val resource = new Resource
    resource.url = url
    resource.content = content
    resource.mimeType = mimeType
    model.persist(resource)
  }

  def entityId(e: Resource) = e.url

  def entityClass = classOf[Resource]

  def allQueryName = "allResources"
}