package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.ExecutionContext

class Stdlib_Boolean extends RTObject {
	def evaluate(context: ExecutionContext):RTObject = { this }
}