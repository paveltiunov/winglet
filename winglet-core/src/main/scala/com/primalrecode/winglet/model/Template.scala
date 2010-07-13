package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence.{Lob, Id, Entity}
import com.primalrecode.winglet.Model

@Entity
class Template {
  @Id
  @BeanProperty
  var uri:String = _

  @BeanProperty
  @Lob
  var content:String = _
}

object Template {
  def create(uri:String, content:String)(implicit model:Model) = {
    val template = new Template
    template.uri = uri
    template.content = content
    model.persist(template)
  }
}