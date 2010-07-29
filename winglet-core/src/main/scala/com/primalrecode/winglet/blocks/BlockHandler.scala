package com.primalrecode.winglet.blocks

import xml.NodeSeq
import net.liftweb.http.TemplateFinder
import com.primalrecode.winglet.Model
import com.primalrecode.winglet.model.{Page, BlockConfig, Block}

trait BlockHandler[T] {
  private var block:Option[Block] = _

  def editForm(config:Option[T]):NodeSeq

  def save:T

  def render(config:T):NodeSeq
  
  def render:NodeSeq = render(blockConfigFromBlock.get)

  private def blockConfigFromBlock = block.map(b => Block.blockConfig[T](b).config)

  def editForm:NodeSeq = editForm(blockConfigFromBlock)

  private def blockConfig = new BlockConfig[T](getClass.asInstanceOf[Class[BlockHandler[_]]], save)

  def saveToBlock(block:Block)(implicit model:Model) = Block.setBlockConfig(block, blockConfig)

  def addBlockToPage(pageId:String)(implicit model:Model) = {
    val page = model.find(classOf[Page], pageId).get
    val block = new Block
    Block.setBlockConfigToBlock(block, blockConfig)
    page.blocks.add(block)
  }

  protected def getTemplate(name:String) = TemplateFinder.findAnyTemplate("templates-hidden" :: name :: Nil).open_!  
}

object BlockHandler {
  def create(block:Block):BlockHandler[_] = create(Block.blockConfig(block).blockHandlerClass, Some(block))

  def create[T <: BlockHandler[_]](blockHandlerClass:Class[T]):T = create(blockHandlerClass, None)

  def create[T <: BlockHandler[_]](blockHandlerClass:Class[T], block:Option[Block]):T = {
    val instance = blockHandlerClass.newInstance
    instance.block = block
    instance
  }
}