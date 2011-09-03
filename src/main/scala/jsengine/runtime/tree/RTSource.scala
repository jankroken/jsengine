package jsengine.runtime.tree

import jsengine.runtime.library._
import jsengine.runtime._

class RTSource(val statements: List[RTExpression]) {
    def evaluate(env: RTEnvironmentRecord):RTObject = {
    		var retValue: RTObject = Stdlib_Undefined;
    		for (statement <- statements) {
    		  retValue = statement.evaluate(env)
    		  statement match {
    		  case Stdlib_Undefined => Stdlib_Undefined
    		  case _ => Stdlib_Undefined
    		}
    	}
    	retValue
    }
}

object RTSource {
    def apply(statements : List[RTExpression]) = new RTSource(statements)
    
}