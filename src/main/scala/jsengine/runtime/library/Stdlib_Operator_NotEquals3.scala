package jsengine.runtime.library

import jsengine.runtime.tree._
object Stdlib_Operator_NotEquals3 extends RTFunction {

    def call(callObject: CallObject):RTObject = {
       Stdlib_Operator_Equals3.call(callObject) match {
         case Stdlib_Boolean(value) => Stdlib_Boolean(!value)
       }
    }
  	def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
    override def toString = "lib_!=="

}