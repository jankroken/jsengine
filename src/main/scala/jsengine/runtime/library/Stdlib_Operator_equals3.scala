package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Equals3 extends RTFunction {
  def call(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0)
    val val2 = callObject.args(1)
    println("==(%s,%s)".format(val1,val2))
    val result = (val1,val2) match {
      case (Stdlib_Undefined,Stdlib_Undefined) => true
      case (Stdlib_Undefined,_) => false
      case (Stdlib_Boolean(true),Stdlib_Boolean(true)) => true
      case (Stdlib_Boolean(false),Stdlib_Boolean(false)) => true
      case (b1:Stdlib_Boolean,o) => compareNumbers(b1.numberValue,o.numberValue)
      case (o,b2:Stdlib_Boolean) => compareNumbers(o.numberValue,b2.numberValue)
      case (n1:Stdlib_Number,o) => compareNumbers(n1,o.numberValue())
      case (o,n2:Stdlib_Number) => compareNumbers(o.numberValue(),n2)
      case (Stdlib_String(s1,_),Stdlib_String(s2,_)) => (s1 == s2)
      case (Stdlib_String(_,_),_) => false
    }
    Stdlib_Boolean(result)
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_===")

  override def toString = "lib_==="

  private def compareNumbers(number1:Stdlib_Number, number2: Stdlib_Number):Boolean = {
    println("===[num](%s,%s)".format(number1,number2))
    (number1.value,number2.value) match {
      case (NaN,_) => false
      case (PositiveInfinity,PositiveInfinity) => true
      case (NegativeInfinity,NegativeInfinity) => true
      case (PositiveInfinity,_) => false
      case (_,PositiveInfinity) => false
      case (NegativeInfinity,_) => false
      case (_,NegativeInfinity) => false
      case (DoubleValue(d1),DoubleValue(d2)) => (d1 == d2)
    }
  }
}