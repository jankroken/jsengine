package jsengine.runtime.tree

import jsengine.runtime.library._

class RTObjectPropertyProxy(val obj: RTObject, index: RTObject) extends RTReferenceType {
	var strict_reference: Boolean = false
	
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	def setValue(value: RTObject) {
    obj.setProperty(index,value)
	}

  def getValue() {
    obj.getProperty(index)
  }
	
	override def valueOf() = {
    val optionalReference = obj.getProperty(index)
    optionalReference match {
      case None => Stdlib_Undefined
      case Some(ref) => ref.value
    }
  }
  override def toBoolean() = { throw new RuntimeException("should never be called") }
	
  override def isObject = { throw new RuntimeException("should never be called") }
	override def isPrimitive = { throw new RuntimeException("should never be called")}
  override def toString = "objectproperty(%s,%s)".format(obj,index)

}

object RTObjectPropertyProxy {
    def apply(obj: RTObject, index: RTObject) = {
    	new RTObjectPropertyProxy(obj,index)
    }
}