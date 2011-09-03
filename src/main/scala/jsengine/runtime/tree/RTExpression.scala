package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

trait RTExpression {
	def evaluate(env: RTEnvironmentRecord):RTObject
}