package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Undefined extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
}