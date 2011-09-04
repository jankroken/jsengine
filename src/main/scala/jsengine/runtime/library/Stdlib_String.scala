package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_String(value: String) extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeStringValue = value
}

object Stdlib_String {
    def apply(value: String) = new Stdlib_String(value)
}