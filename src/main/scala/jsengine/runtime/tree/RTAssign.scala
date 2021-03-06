package jsengine.runtime.tree

import jsengine.runtime.library._

class RTAssign(left: RTExpression, value: RTExpression) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
  		val realValue = value.evaluate(env)
  		val ref = left.evaluate(env)
  		ref match {
  		  	case reference: RTReference => reference.setValue(realValue) 
  		  	case noref: RTNoReference => {
  		  	  // assign var in global object
  		  	  Stdlib_Undefined
  		  	}
          case property: RTObjectPropertyProxy => {
              property.setValue(realValue)
          }
  		  	case _ => throw new RTTypeError("Not a reference: "+ref)
  		}
  		Stdlib_Undefined
  		// 
  	}
  	
  	override def toString:String = s"assign($left,$value)"
}

object RTAssign {
  def apply(left: RTExpression, value: RTExpression) = new RTAssign(left, value)
}