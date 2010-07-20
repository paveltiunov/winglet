package com.primalrecode.winglet.snippet

import net.liftweb.util.BindHelpers
import java.lang.String
import xml._
import net.liftweb.http.js.{JsExp, JsCmds, JE, JsCmd}
import net.liftweb.http.{S, SHtml, FileParamHolder}
import net.liftweb.common.Full
import net.liftweb.http.js.jquery.JqJsCmds

trait EntityEditor[E] extends BindHelpers {
  private val entityListId = "entityList"

  def editForm(originalUri:String, listRefreshCmd: () => JsCmd)(in: NodeSeq) = {
    val bindParams: Seq[BindParam] = submitBindParam(listRefreshCmd) :: cancelBindParam :: entityFields.map(f => f.editorFieldWithId match {
      case Seq(e:Elem) => f.name -%> e
      case nodes => f.name -> nodes
    })
    wrapInForm(originalUri, bind("f", in, bindParams: _*))
  }

  def wrapInForm(originalUri:String, in:NodeSeq) = if (isAjaxForm) SHtml.ajaxForm(in) else <form method="post" enctype="multipart/form-data" action={originalUri}>{in}</form>

  def submitBindParam(listRefreshCmd: () => JsCmd): BindParam = "submit" -%> (
    if (isAjaxForm) SHtml.ajaxSubmit("Save", () => {
      saveEntity()
      listRefreshCmd() & JqJsCmds.Unblock
    })
    else SHtml.submit("Save", saveEntity))
  
  def cancelBindParam():BindParam = "cancel" -%> SHtml.ajaxButton("Cancel", () => JqJsCmds.Unblock)

  def isAjaxForm:Boolean = !containFileEntity(entityFields)

  private def containFileEntity(fields: List[EntityField[E]]):Boolean = fields match {
    case Nil => false
    case x :: xs => if (x.isInstanceOf[FileEntityField[E]]) true else containFileEntity(xs)
  }

  def entityFields: List[EntityField[E]]

  def saveEntity()

  def allEntities: Seq[E]

  private def chooseEditFormTemplate(in:NodeSeq) = chooseTemplate("e", "editForm", in)
  private def chooseUpdateAreaTemplate(in:NodeSeq) = chooseTemplate("e", "updateArea", in)

  def editor(in: NodeSeq) = bind("e", in, 
    "editForm" -> Nil,
    "addEntity" -%> addEntityLink(chooseEditFormTemplate(in), chooseUpdateAreaTemplate(in)) _,
    "updateArea" -%> updateArea(chooseEditFormTemplate(in)) _
  )

  private def updateArea(editFormTemplate:NodeSeq)(in:NodeSeq) = <div id={entityListId}>{updateAreaContent(editFormTemplate, in)}</div>

  private def updateAreaContent(editFormTemplate:NodeSeq, in:NodeSeq) = bind("e", in,
    "entityList" -> entityList(editFormTemplate, in) _
  )

  private def entityList(editFormTemplate: NodeSeq, updateAreaTemplate:NodeSeq)(in: NodeSeq): NodeSeq = {
    val originalUri = S.uri
    allEntities.flatMap(e => bind(
      "e", in,
      "edit" -> SHtml.a(Text(entityListName(e))) {
        editFormDialog(originalUri, editFormTemplate, updateAreaTemplate) & applyEntityToEditorCmd(e)
      },
      "remove" -%> {
        linkText => SHtml.a(linkText) {
          removeEntity(e)
          entityListRefreshCmd(editFormTemplate, updateAreaTemplate)
        }
      }
      ))
  }

  private def addEntityLink(editFormTemplate: NodeSeq, updateAreaTemplate: NodeSeq)(in:NodeSeq) = {
    val originalUri = S.uri
    SHtml.a(in) {editFormDialog(originalUri, editFormTemplate, updateAreaTemplate)}
  }

  private def editFormDialog(originalUri:String, editFormTemplate: NodeSeq, updateAreaTemplate: NodeSeq) =
    JqJsCmds.ModalDialog(editForm(originalUri,
      () => entityListRefreshCmd(editFormTemplate, updateAreaTemplate))(editFormTemplate),
      JE.JsObj(("width", JE.strToS("60%")), ("top", JE.strToS("20%")), ("left", JE.strToS("20%"))))

  private def applyEntityToEditorCmd(e: E): JsCmd = entityFields.map(_.applyToEditorCmd(e)).reduceLeft(_ & _)

  def entityListName(entity: E): String

  def removeEntity(entity: E): Unit

  private def entityListRefreshCmd(editFormTemplate: NodeSeq, updateAreaTemplate:NodeSeq) =
    JsCmds.SetHtml(entityListId, updateAreaContent(editFormTemplate, updateAreaTemplate))
}

trait EntityField[E] {
  type T
  val name: String
  val fieldId: String = name + "Field"
  val saveFun: T => Unit

  def editorField(saveFun: T => Unit): Elem

  def editorFieldWithId: NodeSeq = editorField(saveFun) % (new UnprefixedAttribute("id", fieldId, Null))

  def applyToEditorCmd(entity: E): JsCmd = JsCmds.SetValById(fieldId, applyToEditorExp(entity))

  def applyToEditorExp(entity: E): JsExp
}

case class TextEntityField[E](name: String, saveFun: String => Unit, fieldValueFun: E => String) extends EntityField[E] {
  type T = String
  def editorField(saveFun: (String) => Unit) = SHtml.text("", saveFun)

  def applyToEditorExp(entity: E) = JE.strToS(fieldValueFun(entity))
}

case class TextAreaEntityField[E](name: String, saveFun: String => Unit, fieldValueFun: E => String) extends EntityField[E] {
  type T = String
  def editorField(saveFun: (String) => Unit) = SHtml.textarea("", saveFun)

  def applyToEditorExp(entity: E) = JE.strToS(fieldValueFun(entity))
}

case class CheckboxEntityField[E](name: String, saveFun: Boolean => Unit, fieldValueFun: E => Boolean) extends EntityField[E] {
  type T = Boolean
  def editorField(saveFun: (Boolean) => Unit) = null

  override def editorFieldWithId = SHtml.checkbox_id(false, saveFun, Full(fieldId))

  def applyToEditorExp(entity: E) = null

  override def applyToEditorCmd(entity: E) = JsCmds.SetExp(JE.CheckedById(fieldId), if (fieldValueFun(entity)) JE.strToS("checked") else JE.JsNull)
}

case class FileEntityField[E](name: String, saveFun: FileParamHolder => Unit) extends EntityField[E] {
  type T = FileParamHolder

  def editorField(saveFun: (FileParamHolder) => Unit) = SHtml.fileUpload(saveFun)

  override def applyToEditorCmd(entity: E) = JsCmds.Noop

  def applyToEditorExp(entity: E) = null
}