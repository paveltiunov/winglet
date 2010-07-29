package com.primalrecode.winglet.blocks

import com.primalrecode.winglet.model.{BlockConfig, Block}
import org.scalatest.junit.ShouldMatchersForJUnit
import com.primalrecode.winglet.Model
import org.mockito.{MockSettings, Mockito}
import org.junit.{Ignore, Test}

class TextBlockTest extends ShouldMatchersForJUnit{
  @Test
  def gutter {
    val block = new Block
    val blockConfig = new BlockConfig[String](classOf[TextBlockHandler], "Hello world")
    Block.setBlockConfigToBlock(block, blockConfig)
    println(block.blockConfigXml)
    var config = Block.blockConfig(block)
    config should be (blockConfig)
  }
}