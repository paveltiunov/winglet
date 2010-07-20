package com.primalrecode.winglet.view

import com.primalrecode.winglet.{Model}
import com.google.inject.Inject
import com.primalrecode.winglet.model.{Block, Page}
import scala.collection.JavaConversions._
import net.liftweb.textile.TextileParser
import net.liftweb.common.Box
import net.liftweb.util.BindHelpers
import net.liftweb.http._
import js.jquery.JqJsCmds
import js.JsCmds.SetHtml
import js.{JE, JsCmd, JsCmds}
import xml.{Text, Elem, NodeSeq}
import com.primalrecode.winglet.auth.isAdminUser

class Pages @Inject() (implicit val model:Model) extends BindHelpers with UriHelpers{
  def dispatch:LiftRules.ViewDispatchPF = {
    case splitUrl if splitUrl.length > 0 && pageExists(splitUrl) => Left(() => renderPage(splitUrl.uri))
  }

  private def pageExists(splitUrl:List[String]) = model.find(classOf[Page], splitUrl.uri).isDefined

  def removeLink(pageId: String, block: Block): Elem = {
    SHtml.a(<div>Remove block</div>) {
      Block.remove(block)
      refreshPageBlocksCmd(pageId)
    }
  }

  def editLink(pageId:String, block:Block, commiter:BlockCommiter): Elem = SHtml.a(Text("Edit block")) {
    editorDialog(getTemplate("blockeditor").open_!, commiter, pageId)
  }

  def editorArea(commiter:BlockCommiter) = <div id={commiter.editorId}></div>

  private def renderBlocks(page: Page):NodeSeq = {
    val pageId = page.url
    page.blocks.flatMap(b => {
      val commiter = new BlockCommiter(pageId, b)
      TextileParser.parse(b.text, None).get.toHtml ++ editButtons(pageId, b, commiter)
    })
  }

  private def editButtons(pageId:String, b:Block, commiter:BlockCommiter) =
    if (isAdminUser.get) editorArea(commiter) ::
          removeLink(pageId, b) ::
          editLink (pageId, b, commiter) ::
          Nil
    else Nil

  private def getTemplate(name:String) = TemplateFinder.findAnyTemplate("templates-hidden" :: name :: Nil) ?~ (name + " template not found")

  private def refreshPageBlocksCmd(pageId:String) = JsCmds.SetHtml("pageblocks", renderBlocks(model.find(classOf[Page], pageId).get))

  def editorDialog(blockEditorTemplate: NodeSeq, commiter: Pages#BlockCommiter, pageId: String): JsCmd = {
    JqJsCmds.ModalDialog(bind(
        "e", blockEditorTemplate,
        "textArea" -%> SHtml.textarea(commiter.blockText, commiter.blockText = _),
        "submit" -> SHtml.ajaxSubmit(
          "Save", () => {
            commiter.commit
            JqJsCmds.Unblock & refreshPageBlocksCmd(pageId)
          }
          ),
        "cancel" -> SHtml.ajaxButton("Cancel", () => JqJsCmds.Unblock)
        )
      , JE.JsObj(("width", JE.strToS("60%")), ("top", JE.strToS("20%")), ("left", JE.strToS("20%"))))
  }

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
      "addBlock" -> addBlockButton (blockEditorTemplate, commiter, pageId)
    )
  }

  private def addBlockButton(blockEditorTemplate:NodeSeq, commiter:BlockCommiter, pageId:String) = if (isAdminUser.get) SHtml.a(<div>Add block</div>) {
    editorDialog(blockEditorTemplate, commiter, pageId)
  } else <div></div>

  class BlockCommiter(pageId:String, var block:Block) { //TODO: extract two classes
    var blockText: String = _
    val editorId:String = ("editor" + (if (block != null) block.encodedKey else ""))
            .replace("\"","_").replace("/","_").replace("(","_").replace(")","_") 

    def this(pageId:String) = this(pageId, null)

    if (block != null) {
      blockText = block.text
    }

    def commit() {
      val page = model.find(classOf[Page], pageId).get
      if (block == null) {
        val block = new Block
        block.text = blockText
        page.blocks.add(block)
      } else {
        Block.updateText(block, blockText)
      }
    }
  }

}