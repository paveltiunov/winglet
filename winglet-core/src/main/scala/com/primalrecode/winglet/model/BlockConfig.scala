package com.primalrecode.winglet.model

import com.primalrecode.winglet.blocks.BlockHandler


case class BlockConfig[T](var blockHandlerClass:Class[_ <: BlockHandler[_]], var config:T) 