package com.primalrecode.winglet.snippet

import net.liftweb.util.BindHelpers
import java.lang.String
import xml._
import net.liftweb.http.js.{JsExp, JsCmds, JE, JsCmd}
import net.liftweb.builtin.snippet.Form
import net.liftweb.http.{S, SHtml, FileParamHolder}

trait EntityEditor[E] extends BindHelpers {
  private val entityListId = "entityList"

  def editForm(listRefreshCmd: () => JsCmd)(in: NodeSeq) = {
    val bindParams: Seq[BindParam] = submitBindParam(listRefreshCmd) :: entityFields.map(f => f.name -%> f.editorField % (new UnprefixedAttribute("id", f.fieldId, Null)))
    wrapInForm(bind("f", in, bindParams: _*))
  }

  def wrapInForm(in:NodeSeq) = if (ajaxForm) SHtml.ajaxForm(in) else <form method="post" enctype="multipart/form-data" action={S.uri}>{in}</form>

  def submitBindParam(listRefreshCmd: () => JsCmd): BindParam = "submit" -%> (
    if (ajaxForm) SHtml.ajaxSubmit("Save", () => {
      saveEntity()
      listRefreshCmd()
    })
    else SHtml.submit("Save", saveEntity))

  def ajaxForm:Boolean = !containFileEntity(entityFields)

  private def containFileEntity(fields: List[EntityField[E]]):Boolean = fields match {
    case Nil => false
    case x :: xs => if (x.isInstanceOf[TextEntityField[E]]) true else containFileEntity(xs)
  }

  def entityFields: List[EntityField[E]]

  def saveEntity()

  def allEntities: Seq[E]

  def editor(in: NodeSeq) = bind("e", in, "entityList" -%> entityList _, "editForm" -%> editForm(() => entityListRefreshCmd(chooseTemplate("e", "entityList", in))) _)

  def entityList(in: NodeSeq): Elem = <div id={entityListId}>
    {entityListContent(in)}
  </div>

  def entityListContent(in: NodeSeq): NodeSeq = allEntities.flatMap(e => bind(
    "e", in,
    "edit" -> SHtml.a(Text(entityListName(e))) {
      applyEntityToEditorCmd(e)
    },
    "remove" -%> {
      linkText => SHtml.a(linkText) {
        removeEntity(e)
        entityListRefreshCmd(in)
      }
    }
    ))

  def applyEntityToEditorCmd(e: E): JsCmd = entityFields.map(_.applyToEditorCmd(e)).reduceLeft(_ & _)

  def entityListName(entity: E): String

  def removeEntity(entity: E): Unit

  def entityListRefreshCmd(entityListTemplate:NodeSeq) = JsCmds.SetHtml(entityListId, entityListContent(entityListTemplate))
}

trait EntityField[E] {
  type T
  val name: String
  val fieldId: String = name + "Field"
  val saveFun: T => Unit

  def editorField(saveFun: T => Unit): Elem

  def editorField: Elem = editorField(saveFun)

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

case class FileEntityField[E](name: String, saveFun: FileParamHolder => Unit) extends EntityField[E] {
  type T = FileParamHolder

  def editorField(saveFun: (FileParamHolder) => Unit) = SHtml.fileUpload(saveFun)

  override def applyToEditorCmd(entity: E) = JsCmds.Noop

  def applyToEditorExp(entity: E) = null
}