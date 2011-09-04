package jsengine.runtime.library

import jsengine.runtime.tree._

class Stdlib_Throw(val expression: RTExpression) extends RTFunction {
        def call(callObject: CallObject):RTObject = {
        	val value = expression.evaluate(callObject.env)
        	throw new RTJSThrowException(value)
        }

	  	def toBoolean: Stdlib_Boolean = { throw new RuntimeException("this should not be called") }
}