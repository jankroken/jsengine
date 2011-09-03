package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

case class RTDeclare(id: RTId) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = { throw new RuntimeException("Not implemented") }
}
