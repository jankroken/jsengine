package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Multiply extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0)
    val val2 = callObject.args(1)
    (val1,val2) match {
      case (num1:Stdlib_Number,num2:Stdlib_Number) => multiplyNumbers(num1,num2)
      case _ => throw new RuntimeException(s"Not implemented: +($val1,$val2)")
    }
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_*")
  override def toString = "lib_*"

  private def multiplyNumbers(number1:Stdlib_Number, number2: Stdlib_Number):Stdlib_Number = {
    val value = (number1.value,number2.value) match {
      case (NaN,_) => NaN
      case (_,NaN) => NaN
      case (PositiveInfinity,NegativeInfinity) => NegativeInfinity
      case (NegativeInfinity,PositiveInfinity) => NegativeInfinity
      case (PositiveInfinity,DoubleValue(n)) if n >= 0  => PositiveInfinity
      case (PositiveInfinity,DoubleValue(n)) if n < 0  => NegativeInfinity
      case (DoubleValue(n),PositiveInfinity) if n >= 0  => PositiveInfinity
      case (DoubleValue(n),PositiveInfinity) if n < 0  => NegativeInfinity
      case (NegativeInfinity,DoubleValue(n)) if n >= 0  => NegativeInfinity
      case (NegativeInfinity,DoubleValue(n)) if n < 0  => PositiveInfinity
      case (DoubleValue(n),NegativeInfinity) if n >= 0  => NegativeInfinity
      case (DoubleValue(n),NegativeInfinity) if n < 0  => PositiveInfinity
      case (DoubleValue(d1),DoubleValue(d2)) => DoubleValue(d1*d2)
    }
    Stdlib_Number(value)
  }
}