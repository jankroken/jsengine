package jsengine.ast

import jsengine.library.BuiltinObjects
import jsengine.library.BuiltinNativeCalls

class JSNativeCall(val identifier: JSString) extends JSExpression {
	def execute:JSObject = {
		return BuiltinObjects._undefined
	}
	
	override def toString():String = {
			return "JSNativeCall("+identifier+")"
	}
	
	override def evaluate():JSObject = {
		return BuiltinNativeCalls.executeNativeCall(identifier.value)
	}
  
}
object JSNativeCall {
  def apply(identifier: JSString):JSNativeCall = {
    return new JSNativeCall(identifier)
  }
}