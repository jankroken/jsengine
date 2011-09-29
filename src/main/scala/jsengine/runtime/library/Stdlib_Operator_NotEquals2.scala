package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_NotEquals2 extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    Stdlib_Operator_Equals2.call(callObject) match {
     case Stdlib_Boolean(value) => Stdlib_Boolean(!value)
    }
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_!=")
  override def toString = "lib_!="

}