package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

case class Stdlib_Boolean(value: Boolean) extends RTObject(None) {
	def evaluate(env: RTEnvironmentRecord):RTObject = this
	def nativeBooleanValue = value
	override def booleanValue: Stdlib_Boolean = this
  override def stringValue = Stdlib_String(""+value)
	override def toString:String = "lib_"+value.toString
	override def isObject = false
	override def isPrimitive = true
  override def typeof = "boolean"
  override def numberValue() = value match {
    case false => Stdlib_Number(0)
    case true => Stdlib_Number(1)
  }
}
