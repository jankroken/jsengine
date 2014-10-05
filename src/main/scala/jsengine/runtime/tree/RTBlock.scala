package jsengine.runtime.tree

class RTBlock(val expressions: List[RTExpression]) extends RTExpression {
	def evaluate(env: RTEnvironmentRecord):RTObject = { throw new RuntimeException("Not implemented") }
}