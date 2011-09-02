package jsengine.runtime

import jsengine.runtime.tree._
import jsengine.runtime.types._

class ExecutionContext(scope: Scope) {
  
	def runSource(source: RTSource) {
		
	}

}

object ExecutionContext {
    def apply():ExecutionContext = {
    	new ExecutionContext(new Scope(None))
    }
}