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
    override def toBoolean() = { Stdlib_Boolean(true) } // @TODO: verify
	
  	override def isObject = false
	override def isPrimitive = false
  override def toString = "ref(" + referenced_name + ")"

}

object RTReference {
    def apply(reference_name: RTId) = {
    	new RTReference(reference_name)
    }
}

case class RTNoReference(val referenced_name: RTId) extends RTReferenceType {
    def evaluate(env: RTEnvironmentRecord):RTObject = { this }
    override def toBoolean():Stdlib_Boolean = { Stdlib_Boolean(false) }
    override def valueOf():RTObject = {
        throw new RTReferenceError(""+referenced_name.value+" is not defined")
    }
  	override def isObject = false
	override def isPrimitive = false

}