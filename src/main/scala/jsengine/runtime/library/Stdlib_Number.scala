package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Number(val value: NumericValue) extends RTObject {

  def evaluate(env: RTEnvironmentRecord): RTObject = this

  def toBoolean: Stdlib_Boolean = {
    throw new RuntimeException("not implemented")
  }

  override def isObject = false
  override def isPrimitive = true
  override def toString():String = ""+value
  override def toNumber = this
}

object Stdlib_Number {
  def apply(value: String) = new Stdlib_Number(DoubleValue(value.toDouble))
  def apply(value: NumericValue) = new Stdlib_Number(value)
  def apply(value: Double) = new Stdlib_Number(DoubleValue(value))
}