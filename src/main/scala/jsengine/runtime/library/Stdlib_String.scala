package jsengine.runtime.library

import jsengine.runtime.tree.{RTNamedObjectProperty, RTObject, RTEnvironmentRecord}

case class Stdlib_String(value: String, isObject: Boolean = false) extends RTObject(Some(Stdlib_Object_String)) {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeStringValue = value
	override def booleanValue: Stdlib_Boolean = { Stdlib_Boolean(value.length() > 0) }
  override def stringValue = this
  override def numberValue = {
    if(value.length == 0) {
      Stdlib_Number(0.0)
    } else {
      try {
        Stdlib_Number(value.toDouble)
      } catch {
        case _:Throwable => Stdlib_Number(NaN)
      }
    }
  }

	override def isPrimitive = true
  override def toString = s"string('$value')"
  override def typeof = "string"

  override def getProperty(key:RTObject):Option[RTNamedObjectProperty] = {
    key match {
      case Stdlib_String("length",_) => Some(RTNamedObjectProperty(Stdlib_Number(value.length)))
      case Stdlib_String("constructor",_) => Some(RTNamedObjectProperty(Stdlib_Object_String))
      case _ => super.getProperty(key)
    }
  }

}