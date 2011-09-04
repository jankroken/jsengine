package jsengine.runtime.tree

import jsengine.runtime.library._

class RTSimpleFunctionCall(functionExpression: RTExpression, args: List[RTExpression]) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
  		val functionValue = functionExpression.evaluate(env)
  		val evaluatedArgs = args.map((x) => x.evaluate(env).valueOf)
  		val callObject = CallObject(env,evaluatedArgs)
  		functionValue match {
		    case function: RTFunction => function.call(callObject)
		    case _ => throw new RuntimeException("not implemented: call:"+functionValue)
  		}
  		
  	}
}

object RTSimpleFunctionCall {
  def apply(functionExpression: RTExpression, args: List[RTExpression]) = {
    new RTSimpleFunctionCall(functionExpression, args)
  }
}