package jsengine.runtime.tree

import jsengine.runtime.library._

case class RTDeclare(id: RTId) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = { 
	  	env.declare(id)
	  	Stdlib_Undefined
	}
}
