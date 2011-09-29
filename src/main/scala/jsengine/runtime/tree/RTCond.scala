package jsengine.runtime.tree

import jsengine.runtime.library._

class RTCond(val cond:RTExpression, val whenTrue:RTExpression, val whenFalse:RTExpression) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
        val selector = cond.evaluate(env).valueOf.booleanValue.nativeBooleanValue
        if (selector) {
            whenTrue.evaluate(env)
        } else {
            whenFalse.evaluate(env)
        }
  	}
  	override def toString():String = {
  	    "cond("+cond+","+whenTrue+","+whenFalse+")"
  	}
  	
}

object RTCond {
  def apply(cond:RTExpression,trueExpr:RTExpression,falseExpr:RTExpression):RTCond = {
    new RTCond(cond,trueExpr,falseExpr)
  }
}