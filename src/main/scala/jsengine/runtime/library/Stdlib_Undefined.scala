package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Undefined extends RTObject(None) {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	override def toString = "<undefined>"
	override def booleanValue = Stdlib_Boolean(false)
  override def stringValue = Stdlib_String("undefined")
	
	override def isObject = false
	override def isPrimitive = true
  override def typeof = "undefined"

}