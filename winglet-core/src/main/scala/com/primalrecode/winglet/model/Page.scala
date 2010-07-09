package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence.{Id, Entity}


@Entity
class Page {
  @BeanProperty
  @Id
  var name: String = _

  @BeanProperty
  var text: String = _
}