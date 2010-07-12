package com.primalrecode.winglet.snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._
import net.liftweb.http.{FileParamHolder, SHtml, StatefulSnippet}
import com.google.inject.Inject
import com.primalrecode.winglet.{Model, Injection}
import com.primalrecode.winglet.model.Resource

class UploadResource extends StatefulSnippet with Injection {
  @Inject
  implicit var model:Model = _

  private var file:FileParamHolder = _
  private var url:String = _

  def dispatch = {
    case "uploadForm" => uploadForm _
  }

  def uploadForm(in:NodeSeq) = bind(
    "f", in,
    "file" -%> SHtml.fileUpload(file = _),
    "url" -%> SHtml.text("", url = _),
    "submit" -%> SHtml.submit("Upload", commitFile)
  )

  private def commitFile() = Resource.create(url, file.file, file.mimeType)
}