package jsengine.runtime.tree

import jsengine.runtime.library._

class RTObjectPropertyProxy(val obj: RTObject, index: RTObject) extends RTObject(None) {
	var strict_reference: Boolean = false
	
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	
	def setValue(value: RTObject) {
//    println("proxy.setValue(%s) <= %s",index,value)
    obj.setProperty(index,value)
	}

  def getValue() {
//   println("proxy.getValue(%s from %s) => %s",index,obj,obj.getProperty(index))
    obj.getProperty(index)
  }
	
	override def valueOf() = {
    val optionalReference = obj.getProperty(index)
//    println("proxy.valueOf (%s . %s) => %s".format(obj, index,optionalReference))
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