package jsengine.runtime.tree

import jsengine.runtime.library._

class RTNewCall(functionExpression: RTExpression, args: List[RTExpression]) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
  		val functionValue = functionExpression.evaluate(env)
  		val evaluatedArgs = args.map((x) => x.evaluate(env).valueOf)
  		val callObject = CallObject(env,evaluatedArgs)
  		functionValue match {
		    case function: RTFunction => function.newCall(callObject)
		    case _ => throw new RuntimeException("not implemented: call:"+functionValue)
  		}	
  	}
  	override def toString():String = {
  	    "new("+functionExpression+","+args+")"
  	}
  	
}

object RTNewCall {
  def apply(functionExpression: RTExpression, args: List[RTExpression]) = {
    new RTNewCall(functionExpression, args)
  }
}