package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_LessThan extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0)
    val val2 = callObject.args(1)
    (val1,val2) match {
      case (num1:Stdlib_Number,o) => compareNumbers(num1,o.numberValue)
      case (o,num2:Stdlib_Number) => compareNumbers(o.numberValue,num2)
      case (b1: Stdlib_Boolean,o) => compareNumbers(b1.numberValue,o.numberValue)
      case (o, b2: Stdlib_Boolean) => compareNumbers(o.numberValue,b2.numberValue)
      case (s1:Stdlib_String, o) => compareStrings(s1,o.stringValue)
      case (o,s2:Stdlib_String) => compareStrings(o.stringValue,s2)
      case _ => throw new RuntimeException("Not implemented: +("+val1+","+val2+")")
    }
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_<")
  override def toString = "lib_<"

  private def compareNumbers(number1:Stdlib_Number, number2: Stdlib_Number) = {
    (number1.value,number2.value) match {
      case (NaN,_) => Stdlib_Boolean(false)
      case (_,NaN) => Stdlib_Boolean(false)
      case (PositiveInfinity,NegativeInfinity) => Stdlib_Boolean(false)
      case (NegativeInfinity,PositiveInfinity) => Stdlib_Boolean(true)
      case (PositiveInfinity,_) => Stdlib_Boolean(false)
      case (_,PositiveInfinity) => Stdlib_Boolean(true)
      case (NegativeInfinity,_) => Stdlib_Boolean(true)
      case (_,NegativeInfinity) => Stdlib_Boolean(false)
      case (DoubleValue(d1),DoubleValue(d2)) => Stdlib_Boolean(d1<d2)
    }
  }

  private def compareStrings(string1:Stdlib_String, string2: Stdlib_String) = {
    val Stdlib_String(s1,_) = string1
    val Stdlib_String(s2,_) = string2
    Stdlib_Boolean(s1 < s2)
  }

}