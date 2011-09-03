package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Boolean extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
}