package jsengine.ast

import jsengine.library.BuiltinObjects

class JSNativeCall(val identifier: JSString) extends JSExpression {
	def execute:JSObject = {
		return BuiltinObjects._undefined
	}
	
	override def toString():String = {
			return "JSNativeCall("+identifier+")"
	}
  
}
object JSNativeCall {
  def apply(identifier: JSString):JSNativeCall = {
    return new JSNativeCall(identifier)
  }
}