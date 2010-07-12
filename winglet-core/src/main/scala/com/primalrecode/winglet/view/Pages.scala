package com.primalrecode.winglet.view

import com.primalrecode.winglet.{Model, Injection}
import com.google.inject.Inject
import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds
import com.primalrecode.winglet.model.{Block, Page}
import java.util.ArrayList
import net.liftweb.http.{S, SHtml, TemplateFinder, LiftView}
import scala.collection.JavaConversions._
import net.liftweb.textile.TextileParser

class Pages extends LiftView with Injection{
  @Inject
  var model:Model = _;

  def dispatch = {
    case pageId => () => renderPage(pageId)
  }

  private def renderBlocks(page:Page) = page.blocks.flatMap(b => TextileParser.parse(b.text, None).get.toHtml)

  private def renderPage(pageId:String) = {
    val page = model.find(classOf[Page], pageId)
    val renderedBlocks = <div id="pageblocks">{renderBlocks(page.get)}</div>
    val commiter = new BlockCommiter(pageId)
    val template = TemplateFinder.findAnyTemplate("templates-hidden" :: "page" :: Nil).open_!
    bind("page", template,
      "blocks" -> renderedBlocks,
      "name" -> page.map(_.name).getOrElse("fail"),
      "addBlock" -> SHtml.a(<div>Add block</div>) {
        JsCmds.SetHtml("editor", bind("e", TemplateFinder.findAnyTemplate("templates-hidden" :: "blockeditor" :: Nil).open_!,
          "textArea" -%> SHtml.textarea("", commiter.blockText = _),
          "submit" -> SHtml.ajaxSubmit("Save", () => {
            commiter.commit
            JsCmds.CmdPair(JsCmds.SetHtml("editor", <div></div>), JsCmds.SetHtml("pageblocks", renderBlocks(model.find(classOf[Page], pageId).get)))
          }))
          )
      }
    )
  }

  class BlockCommiter(pageId:String) {
    var blockText:String = _

    def commit() {
      val page = model.find(classOf[Page], pageId).get
      val block = new Block
      block.text = blockText
      page.blocks.add(block)
    }
  }
}