package com.primalrecode.winglet.model

import com.primalrecode.winglet.Model

trait ModelSugar[T <: AnyRef] {

  def entityClass:Class[T]

  def reload(entity:T)(implicit model:Model) = model.find(entityClass, entityId(entity)).get

  def entityId(e:T):Any

  def remove(e:T)(implicit model:Model) {
    model.inTransaction(() => model.removeAndFlush(reload(e)))
  }

  def all(implicit model:Model) = model.findAll[T](allQueryName)

  def allQueryName:String

  implicit def toSugar(model:Model) = Sugar(model)

  case class Sugar(model:Model) {
    def inTransaction[T](fun: () => T) = {
      val transaction = model.getTransaction
      try {
        transaction.begin
        val ret = fun()
        transaction.commit
        ret
      } finally {
        if (transaction.isActive) transaction.rollback
      }
    }
  }
}