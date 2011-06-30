package jsengine.ast

import jsengine.library.BuiltinObjects

class JSNumber(prototype: PropertySet, properties: Map[JSString,JSObject], val value: Int) extends JSObject(prototype, properties) {

  override def equals(other: Any):Boolean = {
    return other.isInstanceOf[JSNumber] && value == other.asInstanceOf[JSNumber].value
  }
  
  override def hashCode:Int = {
    return value.hashCode()
  }

  override def toString:String = {
    return ""+value
  }
	
  override def evaluate:JSObject = {
	return this
  }
  
}
object JSNumber {
  def apply(value: Int):JSNumber = {
    return new JSNumber(BuiltinObjects._object, Map(), value)
  }
}