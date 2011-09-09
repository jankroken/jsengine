package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Number(value: Double) extends RTObject {
  def evaluate(env: RTEnvironmentRecord): RTObject = {
    this
  }

  def nativeDoubleValue = value

  def toBoolean: Stdlib_Boolean = {
    throw new RuntimeException("not implemented")
  }

  override def isObject = false

  override def isPrimitive = true

  override def toString():String = {
      ""+value
  }

}

object Stdlib_Number {
  def apply(value: String) = {
    new Stdlib_Number(value.toDouble)
  }

  def apply(value: Double) = {
    new Stdlib_Number(value)
  }
}