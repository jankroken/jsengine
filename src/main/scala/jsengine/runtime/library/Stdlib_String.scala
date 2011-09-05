package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_String(value: String) extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeStringValue = value
	def toBoolean: Stdlib_Boolean = { Stdlib_Boolean(value.length() > 0) }
}

object Stdlib_String {
    def apply(value: String) = new Stdlib_String(value)
}