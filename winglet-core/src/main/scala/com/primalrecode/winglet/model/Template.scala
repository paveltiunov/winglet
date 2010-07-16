package com.primalrecode.winglet.model

import reflect.BeanProperty
import com.primalrecode.winglet.Model
import javax.persistence.{NamedQuery, Lob, Id, Entity}

@Entity
@NamedQuery(name = "allTemplates", query = "SELECT t FROM Template t")
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
    val transaction = model.getTransaction
    transaction.begin
    model.persistAndFlush(template)
    transaction.commit
  }

  def allTemplates(implicit model:Model) = model.findAll[Template]("allTemplates")

  def reload(template:Template)(implicit model:Model) = model.find(classOf[Template], template.uri).get

  def remove(template:Template)(implicit model:Model) {
    val transaction = model.getTransaction
    transaction.begin
    model.removeAndFlush(reload(template))
    transaction.commit
  }
}