package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Void extends RTFunction {


  def call(callObject: CallObject):RTObject = {
      val o = callObject.args(0)
      o.valueOf
      Stdlib_Undefined
  }

  def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def toString = "lib_void"

}