package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence.{Lob, Id, Entity}
import com.primalrecode.winglet.Model

@Entity
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

object Resource {
  def create(url:String, content:Array[Byte], mimeType:String) (implicit model:Model) {
    val resource = new Resource
    resource.url = url
    resource.content = content
    resource.mimeType = mimeType
    model.persist(resource)
  }
}