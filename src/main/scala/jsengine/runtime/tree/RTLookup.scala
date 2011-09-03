package jsengine.runtime.tree

import jsengine.runtime.library._

class RTLookup(expr: RTExpression) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
  		Stdlib_Undefined
  	}
}

object RTLookup {
  def apply(left: RTExpression, value: RTExpression) = {
    new RTAssign(left, value)
  }
}