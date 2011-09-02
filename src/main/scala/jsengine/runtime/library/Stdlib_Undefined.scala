package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.ExecutionContext

object Stdlib_Undefined extends RTObject {
	def evaluate(context: ExecutionContext):RTObject = { this }
}