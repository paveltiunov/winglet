package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence._
import com.google.appengine.api.datastore.Key

@Entity
class Block {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var encodedKey:Key = _

  @BeanProperty
  @Lob
  var text:String = _
}