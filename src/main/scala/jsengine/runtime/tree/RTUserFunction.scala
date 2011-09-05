package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class RTUserFunction(val name: Option[RTId],val args: List[RTId],val decl: List[RTId],val source: List[RTExpression]) extends RTFunction {
	var environment: Option[RTEnvironmentRecord] = None
  
  	override def evaluate(env: RTEnvironmentRecord):RTObject = { environment = Some(env); this}
  	override def call(callObject: CallObject):RTObject = {
		var retValue:RTObject = Stdlib_Undefined
		for (expr <- source) {
		  environment match {
		    case None => throw new RuntimeException("environment is not set")
		    case Some(env) => {
		    	retValue = expr.evaluate(env)
		    }
		  }
		}
		retValue
  	}
  	override def toBoolean():Stdlib_Boolean = { Stdlib_Boolean(true) }
  	override def toString():String = {
  		"function("+name+","+args+","+decl+","+source+")"
  	}
}

object RTUserFunction {
	def apply(name: Option[RTId],args: List[RTId],decl: List[RTId],source: List[RTExpression]):RTUserFunction = {
		new RTUserFunction(name,args,decl,source)
	}  
}