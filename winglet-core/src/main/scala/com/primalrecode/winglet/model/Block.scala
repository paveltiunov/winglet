package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence._
import com.google.appengine.api.datastore.Key
import com.primalrecode.winglet.Model

@Entity
class Block {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var encodedKey:Key = _

  @BeanProperty
  @Lob
  var text:String = _
}

object Block extends ModelSugar[Block] {
  def allQueryName = null

  def entityId(e: Block) = e.encodedKey

  def entityClass = classOf[Block]

  def updateText(block: Block, text:String)(implicit model:Model) = model.inTransaction(() => {
    val reloaded = reload(block)
    reloaded.text = text
    model.flush
  })
}