package jsengine.runtime.tree

import jsengine.runtime.library._

class RTLookup(containerExpression: RTExpression, indexExpression: RTExpression) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObjectPropertyProxy = {
      val container = containerExpression.evaluate(env).valueOf
      val index = indexExpression.evaluate(env).valueOf
      println("lookup.eval: (%s,%s)".format(container,index))
      container match {
        case obj: RTObject => {
          RTObjectPropertyProxy(obj,index)
        }
        case _ => throw new RuntimeException("Unhandled property container: "+container)
      }
  	}
    override def toString = "lookup(%s,%s)".format(containerExpression,indexExpression)
}

object RTLookup {
  def apply(left: RTExpression, index: RTExpression) = {
    new RTLookup(left, index)
  }
}