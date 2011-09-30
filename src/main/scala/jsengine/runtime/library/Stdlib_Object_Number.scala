package jsengine.runtime.library

import jsengine.runtime.tree.{RTNamedObjectProperty, RTObject, RTEnvironmentRecord}

object Stdlib_Object_Number extends RTObject(None) {
  override def evaluate(env: RTEnvironmentRecord):RTObject = this
  override def booleanValue = Stdlib_Boolean(true)
  override def stringValue = Stdlib_String("function Number() { <native code> }")
  override def isObject = true
  override def isPrimitive = false
  override def typeof = "function"


  override def setProperty(key:RTObject,value: RTObject) {
    key match {
      case Stdlib_String("NaN",_) => Unit
      case Stdlib_String("NEGATIVE_INFINITY",_) => Unit
      case Stdlib_String("POSITIVE_INFINITY",_) => Unit
      case Stdlib_String("constructor",_) => Unit
      case _ => super.setProperty(key,value)
    }
  }

  override def getProperty(key:RTObject):Option[RTNamedObjectProperty] = {
    key match {
      case Stdlib_String("NaN",_) => Some(RTNamedObjectProperty(Stdlib_Number(NaN)))
      case Stdlib_String("NEGATIVE_INFINITY",_) => Some(RTNamedObjectProperty(Stdlib_Number(NegativeInfinity)))
      case Stdlib_String("POSITIVE_INFINITY",_) => Some(RTNamedObjectProperty(Stdlib_Number(PositiveInfinity)))
      case Stdlib_String("constructor",_) => Some(RTNamedObjectProperty(Stdlib_Object_Number))
      case _ => super.getProperty(key)
    }
  }

}
