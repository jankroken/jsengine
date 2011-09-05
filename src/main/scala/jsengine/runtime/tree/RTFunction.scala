package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

abstract class RTFunction extends RTObject {
  	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
  	def call(callObject: CallObject):RTObject
  	
  	override def isObject = false
	override def isPrimitive = false
}