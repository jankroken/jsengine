package jsengine.ast

import jsengine.library.BuiltinObjects

class JSFunction(val functionName: Option[JSString], val arguments: List[JSString], expressions: List[JSExpression]) 
	extends JSObject(BuiltinObjects._object,Map()) 
{
	override def toString():String = {
		functionName match {
		  case None => return "JSFunction("+'('+arguments+") {"+expressions+"})";
		  case Some(name) => return "JSFunction("+name+'('+arguments+") {"+expressions+"}";
		}
	}	
}

object JSFunction {
	def apply(name: Option[JSString], arguments: List[JSString], expressions: List[JSExpression]):JSFunction = {
		return new JSFunction(name, arguments, expressions)
	}
}
