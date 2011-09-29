package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.library._
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Null extends RTObject(None) {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	override def toString = "<undefined>"
	override def booleanValue = Stdlib_Boolean(false)
  override def stringValue = Stdlib_String("null")
	
	override def isObject = false
	override def isPrimitive = true
  override def typeof = "object"
}