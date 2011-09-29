package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

object Stdlib_Object_Function extends RTObject(None) {
  override def evaluate(env: RTEnvironmentRecord):RTObject = this
  override def stringValue = Stdlib_String("function Function() { <native code> }")
  override def booleanValue = Stdlib_Boolean(true)
  override def isObject = true
  override def isPrimitive = false
  override def typeof = "object"
}
