package com.primalrecode.winglet.view

import com.primalrecode.winglet.{Model, Injection}
import com.google.inject.Inject
import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.js.JsCmds
import com.primalrecode.winglet.model.{Block, Page}
import net.liftweb.http.{S, SHtml, TemplateFinder, LiftView}
import scala.collection.JavaConversions._
import net.liftweb.textile.TextileParser
import net.liftweb.common.Box
import xml.NodeSeq

class Pages extends LiftView with Injection {
  @Inject
  var model: Model = _;

  def dispatch = {
    case pageId => () => renderPage(pageId)
  }

  private def renderBlocks(page: Page):NodeSeq = {
    val pageId = page.name
    page.blocks.flatMap(b => {
      TextileParser.parse(b.text, None).get.toHtml ++ SHtml.a(<div>Remove block</div>) {
        val pageWithBlockToRemove = model.find(classOf[Page], pageId).get
        val toRemove = pageWithBlockToRemove.blocks.find(_.encodedKey == b.encodedKey).get
        pageWithBlockToRemove.blocks.remove(toRemove)
        refreshPageBlocksCmd(pageId)
      }
    })
  }

  private def getTemplate(name:String) = TemplateFinder.findAnyTemplate("templates-hidden" :: name :: Nil) ?~ (name + " template not found")

  private def refreshPageBlocksCmd(pageId:String) = JsCmds.SetHtml("pageblocks", renderBlocks(model.find(classOf[Page], pageId).get))

  private def renderPage(pageId: String) = {
    for{
      page <- Box(model.find(classOf[Page], pageId)) ?~ ("Page not found")
      template <- getTemplate("page")
      blockEditorTemplate <- getTemplate("blockeditor")
      val renderedBlocks = <div id="pageblocks">{renderBlocks(page)}</div>
      val commiter = new BlockCommiter(pageId)
    } yield bind(
      "page", template,
      "blocks" -> renderedBlocks,
      "name" -> page.name,
      "addBlock" -> SHtml.a(<div>Add block</div>) {
        JsCmds.SetHtml(
          "editor", bind(
            "e", blockEditorTemplate,
            "textArea" -%> SHtml.textarea("", commiter.blockText = _),
            "submit" -> SHtml.ajaxSubmit(
              "Save", () => {
                commiter.commit
                JsCmds.CmdPair(JsCmds.SetHtml("editor", <div></div>), refreshPageBlocksCmd(pageId))
              }
            )
          )
        )
      }
    )
  }

  class BlockCommiter(pageId: String) {
    var blockText: String = _

    def commit() {
      val page = model.find(classOf[Page], pageId).get
      val block = new Block
      block.text = blockText
      page.blocks.add(block)
    }
  }

}