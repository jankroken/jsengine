package jsengine.runtime.tree

import jsengine.runtime.library._

abstract class  RTReferenceType extends RTObject(None)

class RTReference(val referenced_name: RTId) extends RTReferenceType {
	var value: RTObject = Stdlib_Undefined
	var strict_reference = false
  override def typeof = "internal:reference"
  override def stringValue = Stdlib_String("internal:reference")
	
	def evaluate(env: RTEnvironmentRecord):RTObject = this
	
	def setValue(value: RTObject) {
		this.value = value
	}
	
	override def valueOf = value
  override def booleanValue = Stdlib_Boolean(true) // @TODO: verify
	
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
  def evaluate(env: RTEnvironmentRecord):RTObject = this
  override def booleanValue() = Stdlib_Boolean(false)
  override def stringValue = Stdlib_String("internal:noreference")
  override def valueOf:RTObject = {
      throw new RTReferenceError(s"${referenced_name.value} is not defined")
  }
  override def isObject = false
	override def isPrimitive = false
  override def typeof = "internal:noreference"

}