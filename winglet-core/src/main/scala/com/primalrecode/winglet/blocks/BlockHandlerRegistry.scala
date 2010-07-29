package com.primalrecode.winglet.blocks


class BlockHandlerRegistry {
  var blockHandlerClassToName:Map[Class[_ <: BlockHandler[_]], String] = Map(
    classOf[TextBlockHandler] -> "Text block",
    classOf[NewsBlockHandler] -> "News block"
  )

  def addHandler(blockHandlerClass:Class[_ <: BlockHandler[_]], name:String) = blockHandlerClassToName += (blockHandlerClass -> name)
}