package jsengine.runtime.tree

class RTSimpleFunctionCall(functionExpression: RTExpression, args: List[RTExpression]) extends RTExpression {
  def evaluate(env: RTEnvironmentRecord):RTObject = {
    println("calling(1) "+functionExpression)
    val function = functionExpression.evaluate(env)
    val thisObject:Option[RTObject] = function match {
      case property: RTObjectPropertyProxy => Some(property.obj)
      case _ => None
    }
    val functionValue = function.valueOf
    println("calling(2) "+functionValue)
    val evaluatedArgs = args.map((x) => x.evaluate(env).valueOf)
    val callObject = new CallObject(env,evaluatedArgs,thisObject)
    val retValue = functionValue match {
      case function: RTFunction => function.call(callObject)
      case _ => throw new RuntimeException("not implemented: call:"+functionValue)
    }
    println("calling(3) "+retValue)
    retValue
  }
  override def toString = "call("+functionExpression+","+args+")"

}

object RTSimpleFunctionCall {
  def apply(functionExpression: RTExpression, args: List[RTExpression]) = {
    new RTSimpleFunctionCall(functionExpression, args)
  }
}