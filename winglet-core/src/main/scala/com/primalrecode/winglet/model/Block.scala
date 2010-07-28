package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence._
import com.google.appengine.api.datastore.Key
import com.primalrecode.winglet.Model
import com.thoughtworks.xstream.XStream

@Entity
class Block {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var encodedKey:Key = _

  @BeanProperty
  @Lob
  var text:String = _

  @BeanProperty
  @Lob
  var blockConfigXml:String = _
}

object Block extends ModelSugar[Block] {
  private val xstream = {
    val xStream = new XStream
    xStream.alias("BlockConfig", classOf[BlockConfig[_]])
    xStream
  }

  def allQueryName = null

  def entityId(e: Block) = e.encodedKey

  def entityClass = classOf[Block]

  def updateText(block: Block, text:String)(implicit model:Model) = model.inTransaction(() => {
    val reloaded = reload(block)
    reloaded.text = text
    model.flush
  })

  def blockConfig[T](block: Block):BlockConfig[T] = xstream.fromXML(block.blockConfigXml).asInstanceOf[BlockConfig[T]]

  def setBlockConfig(block: Block, blockConfig:BlockConfig[_]) = block.blockConfigXml = xstream.toXML(blockConfig)
}