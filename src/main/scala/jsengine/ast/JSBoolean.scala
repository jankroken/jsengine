package jsengine.ast

import jsengine.library.BuiltinObjects

class JSBoolean(prototype: PropertySet, properties: Map[JSString,JSExpression], val value: Boolean) extends JSObject(prototype, properties) {

  override def equals(other: Any):Boolean = {
    return other.isInstanceOf[JSBoolean] && value == other.asInstanceOf[JSBoolean].value
  }
  
  override def hashCode:Int = {
    return value.hashCode()
  }

  override def toString:String = {
    return ""+value
  }
  
  override def evaluate:JSObject = {
    return this;
  }
}
object JSBoolean {
  def apply(value: Boolean):JSBoolean = {
    value match {
      case true => return BuiltinObjects._true
      case false => return BuiltinObjects._false
    }
  }
}