package jsengine.runtime.tree

class RTGetReferenceById(id: RTId) extends RTExpression {
  	def evaluate(env: RTEnvironmentRecord):RTObject = env.getReference(id)
  	override def toString = s"ref($id)"
}

object RTGetReferenceById {
  def apply(id: RTId) = new RTGetReferenceById(id)
}