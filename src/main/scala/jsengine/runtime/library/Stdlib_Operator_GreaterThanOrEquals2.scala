package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_GreaterThanOrEquals2 extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    Stdlib_Operator_GreaterThan.call(callObject) match {
      case t @ Stdlib_Boolean(true) => t
      case _ => Stdlib_Operator_Equals2.call(callObject)
    }
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_>=")
  override def toString = "lib_>="

}