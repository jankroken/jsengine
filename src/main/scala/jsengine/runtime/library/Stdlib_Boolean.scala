package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Boolean(value: Boolean) extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeBooleanValue = value
	def toBoolean: Stdlib_Boolean = this

}

object Stdlib_Boolean {
    def apply(value: Boolean) = new Stdlib_Boolean(value)
}