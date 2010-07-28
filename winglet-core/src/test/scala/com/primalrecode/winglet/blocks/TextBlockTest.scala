package com.primalrecode.winglet.blocks

import org.junit.Test
import com.primalrecode.winglet.model.{BlockConfig, Block}
import org.scalatest.junit.ShouldMatchersForJUnit

class TextBlockTest extends ShouldMatchersForJUnit{
  @Test
  def gutter {
    val block = new Block
    val blockConfig = new BlockConfig[String](classOf[TextBlockHandler], "Hello world")
    Block.setBlockConfig(block, blockConfig)
    println(block.blockConfigXml)
    Block.blockConfig(block) should be (blockConfig)
  }
}