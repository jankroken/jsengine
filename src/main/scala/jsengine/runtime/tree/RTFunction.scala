package jsengine.runtime.tree

import jsengine.runtime.library.Stdlib_Object_Function

abstract class RTFunction extends RTObject(Some(Stdlib_Object_Function)) {
  	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
  	def call(callObject: CallObject):RTObject
  	
  	override def isObject = false
  override def typeof = "function"
	override def isPrimitive = false
}