package com.primalrecode.winglet.view

trait UriHelpers {

  implicit def toUriSugar(splitUri:List[String]) = UriSugar(splitUri)

  def splitUri(uri:String) = if (!uri.contains("/")) List(uri) else uri.split('/').toList

  case class UriSugar(splitUri:List[String]) {
    def uri:String = splitUri.mkString("/")
  }
}