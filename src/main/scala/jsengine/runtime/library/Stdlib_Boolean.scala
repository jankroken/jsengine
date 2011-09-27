package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

case class Stdlib_Boolean(value: Boolean) extends RTObject(None) {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeBooleanValue = value
	def toBoolean: Stdlib_Boolean = this
	override def toString:String = "lib_"+value.toString
	override def isObject = false
	override def isPrimitive = true
  override def typeof = "boolean"
}
