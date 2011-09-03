package jsengine.runtime.tree

import jsengine.runtime.library._

trait RTReferenceType extends RTObject
class RTReference(val referenced_name: RTId) extends RTReferenceType {
	var value: RTObject = Stdlib_Undefined
	var strict_reference: Boolean = false
	
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	def setValue(value: RTObject) {
		this.value = value;
	}
	
	override def valueOf = value
}

object RTReference {
    def apply(reference_name: RTId) = {
    	new RTReference(reference_name)
    }
}

case class RTNoReference() extends RTReferenceType {
    def evaluate(env: RTEnvironmentRecord):RTObject = { this }
}