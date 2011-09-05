package jsengine.runtime.tree

import jsengine.runtime.library._

class RTGetReferenceById(id: RTId) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = {
  		env.getReference(id)
  	}
  	override def toString():String = {
  		"ref("+id+")"
  	}
}

object RTGetReferenceById {
  def apply(id: RTId) = {
      new RTGetReferenceById(id)
    }
}