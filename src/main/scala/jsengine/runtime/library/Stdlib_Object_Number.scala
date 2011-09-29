package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Object_Number extends RTObject(None) {
  override def evaluate(env: RTEnvironmentRecord):RTObject = this
  override def booleanValue = Stdlib_Boolean(true)
  override def stringValue = Stdlib_String("function Number() { <native code> }")
  override def isObject = true
  override def isPrimitive = false
  override def typeof = "function"

}
