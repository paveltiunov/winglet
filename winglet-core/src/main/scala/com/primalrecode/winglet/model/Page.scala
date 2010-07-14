package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence.{NamedQuery, Id, Entity}
import java.util.List

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