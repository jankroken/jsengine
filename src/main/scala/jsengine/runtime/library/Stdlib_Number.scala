package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Number(val value: NumericValue) extends RTObject(Some(Stdlib_Object)) {

  def evaluate(env: RTEnvironmentRecord): RTObject = this

  def booleanValue: Stdlib_Boolean = {
    throw new RuntimeException("not implemented")
  }

  override def isObject = false
  override def isPrimitive = true
  override def toString():String = ""+value
  override def numberValue = this
  override def stringValue = Stdlib_String(toString)
  override def typeof = "number"
  def isDoubleValue = value match {
    case double: DoubleValue => true
    case _ => false
  }

  def forcedDoubleValue:Double = {
    value match {
      case DoubleValue(dv) => dv
      case _ => throw new RuntimeException("forcedDoubleValue: not a double value: "+value)
    }
  }
}

object Stdlib_Number {
  def apply(value: String) = new Stdlib_Number(DoubleValue(value.toDouble))
  def apply(value: NumericValue) = new Stdlib_Number(value)
  def apply(value: Double) = new Stdlib_Number(DoubleValue(value))
}