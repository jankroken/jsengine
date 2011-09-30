package jsengine.runtime.library

import jsengine.runtime.tree._
import jsengine.runtime.library.Stdlib_Boolean

object Stdlib_Operator_LogicalOr extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    val Stdlib_Boolean(val1) = callObject.args(0).valueOf.booleanValue;
    val Stdlib_Boolean(val2) = callObject.args(1).valueOf.booleanValue;
    Stdlib_Boolean(val1 || val2)
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_||")
  override def toString = "lib_||"

}