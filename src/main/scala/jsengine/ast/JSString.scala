package jsengine.ast

import jsengine.library.BuiltinObjects

class JSString(prototype: PropertySet, properties: Map[JSString,JSObject], val value: String) extends JSObject(prototype, properties) {

  override def equals(other: Any):Boolean = {
    return other.isInstanceOf[JSString] && value == other.asInstanceOf[JSString].value
  }
  
  override def hashCode:Int = {
    return value.hashCode()
  }

  override def toString:String = {
    return "'"+value+"'"
  }
  
}
object JSString {
  def apply(value: String):JSString = {
    return new JSString(BuiltinObjects._object, Map(), value)
  }
}