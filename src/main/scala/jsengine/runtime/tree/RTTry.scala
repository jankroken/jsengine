package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

class RTTry() extends RTExpression {
		def evaluate(context: ExecutionContext):RTObject = { throw new RuntimeException("Not implemented") }

}