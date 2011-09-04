package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class RTUserFunction extends RTObject {
  	def evaluate(env: RTEnvironmentRecord):RTObject = { throw new RuntimeException("Not implemented") }
  	def call(callObject: CallObject):RTExpression = {
  			throw new RuntimeException("not implemented")
  	}
  	override def toBoolean():Stdlib_Boolean = { Stdlib_Boolean(true) }
}