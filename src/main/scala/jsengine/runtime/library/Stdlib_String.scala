package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_String(value: String) extends RTObject(Some(Stdlib_Object)) {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeStringValue = value
	def toBoolean: Stdlib_Boolean = { Stdlib_Boolean(value.length() > 0) }

	override def isObject = false
	override def isPrimitive = true
  override def toString = "string('"+value+")"
}

object Stdlib_String {
    def apply(value: String) = new Stdlib_String(value)
}