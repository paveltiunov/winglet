package com.primalrecode.winglet.model

import com.primalrecode.winglet.blocks.BlockHandler


case class BlockConfig[T](val blockHandlerClassName:String, val blockContent:T) {
  @transient
  val blockHandlerClass:Class[BlockHandler] = Class.forName(blockHandlerClassName).asInstanceOf[Class[BlockHandler]]
  def this(blockHandlerClass:Class[_ <: BlockHandler], blockContent:T) = this(blockHandlerClass.getName, blockContent)
}