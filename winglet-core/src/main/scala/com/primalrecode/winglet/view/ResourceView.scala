package com.primalrecode.winglet.view

import com.primalrecode.winglet.Model
import com.google.inject.Inject
import com.primalrecode.winglet.model.Resource
import net.liftweb.common.{Box}
import net.liftweb.http.{LiftRules, InMemoryResponse, Req}

class ResourceView @Inject() (model:Model) {
  def dispatch:LiftRules.DispatchPF = {
    case r:Req if resourceExists(r.uri) => () => getResource(r.uri)
  }

  def findResource(uri:String) = Box(model.find(classOf[Resource], uri))
  def resourceExists(uri:String) = findResource(uri).isDefined
  def getResource(uri:String) = findResource(uri).map(r => new InMemoryResponse(r.content, ("Content-Type" -> r.mimeType) :: Nil, Nil, 200))
}