package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_BooleanAnd extends RTFunction {

    def call(callObject: CallObject):RTObject = {
        val val1 = callObject.args(0).toBoolean
        if (!val1.nativeBooleanValue) {
        	Stdlib_Boolean(false)
        } else {
        	callObject.args(1).toBoolean
        }
        	
        
    }
  	def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }

}