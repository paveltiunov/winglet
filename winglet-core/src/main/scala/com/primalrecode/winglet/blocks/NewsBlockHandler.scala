package com.primalrecode.winglet.blocks

import net.liftweb.util.BindHelpers
import net.liftweb.http.SHtml


class NewsBlockHandler extends BlockHandler[NewsConfig] with BindHelpers{
  private var title:String = ""

  def render(config: NewsConfig) = bind("n", getTemplate("newsblock"),
    "title" -> config.title
  )

  def save = new NewsConfig(title)

  def editForm(config: Option[NewsConfig]) = {
    config.foreach(c => title = c.title)
    bind("e", getTemplate("newsblockeditor"),
      "title" -> SHtml.text(title, title = _)
    )
  }
}

case class NewsConfig(var title:String)