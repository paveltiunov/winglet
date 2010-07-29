package com.primalrecode.winglet.model

import reflect.BeanProperty
import javax.persistence._
import com.google.appengine.api.datastore.Key
import com.primalrecode.winglet.Model
import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider
import com.primalrecode.winglet.util.GaeXStream

@Entity
class Block {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var encodedKey:Key = _

  @BeanProperty
  @Lob
  var blockConfigXml:String = _
}

object Block extends ModelSugar[Block] {
  private val xstream = {
    val xStream = GaeXStream.create
    xStream.alias("BlockConfig", classOf[BlockConfig[_]])
    xStream
  }

  def allQueryName = null

  def entityId(e: Block) = e.encodedKey

  def entityClass = classOf[Block]

  def blockConfig[T](block: Block):BlockConfig[T] = {
    var blockConfig = xstream.fromXML(block.blockConfigXml).asInstanceOf[BlockConfig[T]]
    blockConfig
  }

  def setBlockConfigToBlock(block: Block, blockConfig:BlockConfig[_]) = block.blockConfigXml = xstream.toXML(blockConfig)

  def setBlockConfig(block: Block, blockConfig:BlockConfig[_])(implicit model:Model) = model.inTransaction(() => {
    val reloaded = reload(block)
    setBlockConfigToBlock(reloaded, blockConfig)
    model.flush
  })
}