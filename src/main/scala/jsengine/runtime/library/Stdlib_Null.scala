package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.library._
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Null extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	override def toString = "<undefined>"
	def toBoolean: Stdlib_Boolean = { Stdlib_Boolean(false)  }
	
	override def isObject = false
	override def isPrimitive = true
}