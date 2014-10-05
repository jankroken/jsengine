package jsengine.runtime.tree

trait RTExpression {
	def evaluate(env: RTEnvironmentRecord):RTObject
}