package com.primalrecode.winglet.snippet

import net.liftweb.util.BindHelpers
import net.liftweb.http.SHtml
import java.lang.String
import xml._
import net.liftweb.http.js.{JsExp, JsCmds, JE, JsCmd}

trait EntityEditor[E] extends BindHelpers {
  private val entityListId = "entityList"

  def editForm(listRefreshCmd: () => JsCmd)(in: NodeSeq) = {
    val bindParams: Seq[BindParam] = submitBindParam(listRefreshCmd) :: entityFields.map(f => f.name -%> f.editorField % (new UnprefixedAttribute("id", f.fieldId, Null)))
    SHtml.ajaxForm(bind("f", in, bindParams: _*))
  }

  def submitBindParam(listRefreshCmd: () => JsCmd): BindParam = "submit" -%> SHtml.ajaxSubmit("Save", () => {
    saveEntity()
    listRefreshCmd()
  })

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
  val saveFun: T => Any
  val fieldValueFun: E => T

  def editorField(saveFun: T => Any): Elem

  def editorField: Elem = editorField(saveFun)

  def applyToEditorCmd(entity: E): JsCmd = JsCmds.SetValById(fieldId, applyToEditorExp(entity))

  def applyToEditorExp(entity: E): JsExp
}

case class TextEntityField[E](name: String, saveFun: String => Any, fieldValueFun: E => String) extends EntityField[E] {
  type T = String
  def editorField(saveFun: (String) => Any) = SHtml.text("", saveFun)

  def applyToEditorExp(entity: E) = JE.strToS(fieldValueFun(entity))
}

case class TextAreaEntityField[E](name: String, saveFun: String => Any, fieldValueFun: E => String) extends EntityField[E] {
  type T = String
  def editorField(saveFun: (String) => Any) = SHtml.textarea("", saveFun)

  def applyToEditorExp(entity: E) = JE.strToS(fieldValueFun(entity))
}