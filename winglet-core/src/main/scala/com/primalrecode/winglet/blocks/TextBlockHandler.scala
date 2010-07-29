package com.primalrecode.winglet.blocks

import java.lang.String
import net.liftweb.util.BindHelpers
import net.liftweb.http.SHtml
import net.liftweb.textile.TextileParser


class TextBlockHandler extends BlockHandler[String] with BindHelpers {
  private var text:String = _
  
  def save = text

  def editForm(config: Option[String]) = {
    config.foreach(text = _)
    bind("e", getTemplate("textblockeditor"),
      "textArea" -%> SHtml.textarea(text, text = _)
    )
  }

  def render(config: String) = TextileParser.parse(config, None).get.toHtml
}