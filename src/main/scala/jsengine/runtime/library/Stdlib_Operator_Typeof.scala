package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Typeof extends RTFunction {


  def call(callObject: CallObject):RTObject = {
    val o:RTObject = callObject.args(0)
    Stdlib_String(o.typeof)
  }

  def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def toString = "lib_typeof"

}