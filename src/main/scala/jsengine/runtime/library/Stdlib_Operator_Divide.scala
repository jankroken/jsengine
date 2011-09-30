package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Divide extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0).valueOf.numberValue
    val val2 = callObject.args(1).valueOf.numberValue
    println("/(%s,%s)".format(val1,val2))
    (val1.value,val2.value) match {
      case (NaN,_) => Stdlib_Number(NaN)
      case (_,NaN) => Stdlib_Number(NaN)
      case (PositiveInfinity,PositiveInfinity) => Stdlib_Number(NaN)
      case (PositiveInfinity,NegativeInfinity) => Stdlib_Number(NaN)
      case (NegativeInfinity,PositiveInfinity) => Stdlib_Number(NaN)
      case (NegativeInfinity,NegativeInfinity) => Stdlib_Number(NaN)
      case (PositiveInfinity,value2) => if (isPositive(val2)) { Stdlib_Number(PositiveInfinity) } else { Stdlib_Number(NegativeInfinity) }
      case (value1,PositiveInfinity) => if (isPositive(val1)) { Stdlib_Number(PositiveInfinity) } else { Stdlib_Number(NegativeInfinity) }
      case (NegativeInfinity,value2) => if (isPositive(val2)) { Stdlib_Number(NegativeInfinity) } else { Stdlib_Number(PositiveInfinity) }
      case (value1,NegativeInfinity) => if (isPositive(val1)) { Stdlib_Number(NegativeInfinity) } else { Stdlib_Number(PositiveInfinity) }
      case (DoubleValue(0.0),DoubleValue(0.0)) => Stdlib_Number(NaN)
      case (value1,DoubleValue(0.0)) => if (isPositive(val1) ^ isPositive(val2)) { Stdlib_Number(NegativeInfinity)} else { Stdlib_Number(PositiveInfinity)}
      case (DoubleValue(d1),DoubleValue(d2)) => Stdlib_Number(DoubleValue(d1/d2),!(val1.negated ^ val2.negated))
    }
  }

  private def isPositive(number: Stdlib_Number):Boolean = {
    val Stdlib_Number(value,negated) = number
    val valuePositive = value match {
      case PositiveInfinity => true
      case NegativeInfinity => false
      case NaN => true
      case DoubleValue(d1) => d1 >= 0.0
    }
    if (negated) {
      !(valuePositive)
    } else {
      valuePositive
    }
  }

  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_/")

  override def toString = "lib_/"

  private def compareNumbers(number1:Stdlib_Number, number2: Stdlib_Number):Boolean = {
    println("/[num](%s,%s)".format(number1,number2))
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