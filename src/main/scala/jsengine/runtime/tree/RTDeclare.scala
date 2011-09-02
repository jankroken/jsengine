package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

case class RTDeclare(id: RTId) extends RTExpression {
  	def evaluate(context: ExecutionContext):RTObject = { throw new RuntimeException("Not implemented") }
}
