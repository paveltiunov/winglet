package com.primalrecode.winglet.view

import com.google.inject.Inject
import com.primalrecode.winglet.Model
import net.liftweb.http.LiftRules
import com.primalrecode.winglet.model.Template
import net.liftweb.common.Box
import net.liftweb.util.PCDataXmlParser

class TemplateView @Inject() (model:Model) {
  def dispatch:LiftRules.ViewDispatchPF = {
    case splitUrl if splitUrl.length > 0 && templateExists(splitUrl) => Left(() => getTemplate(splitUrl))
  }

  def findTemplate(splitUrl:List[String]) = Box(model.find(classOf[Template], splitUrl.mkString("/")))
  def templateExists(splitUrl:List[String]) = findTemplate(splitUrl).isDefined
  def getTemplate(splitUrl:List[String]) = findTemplate(splitUrl).flatMap(t => PCDataXmlParser(t.content))
}