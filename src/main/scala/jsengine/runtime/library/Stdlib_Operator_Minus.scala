package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Minus extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    callObject.args.length match {
      case 1 => callUnary(callObject)
      case 2 => callBinary(callObject)
      case _ => throw new RuntimeException("Builtin: - : Wrong number of arguments(%s):%s".format(callObject.args.length,callObject.args))
    }
  }

  def callUnary(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0).numberValue()
    return negate(val1)
  }



  def callBinary(callObject: CallObject):RTObject = {
    val val1 = callObject.args(0).numberValue()
    val val2 = callObject.args(1).numberValue()
    return subNumbers(val1,val2)
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_-")
  override def toString = "lib_-"

  private def subNumbers(number1:Stdlib_Number, number2: Stdlib_Number):Stdlib_Number = {
    (number1.value,number2.value) match {
      case (NaN,_) => Stdlib_Number(NaN)
      case (_,NaN) => Stdlib_Number(NaN)
      case (PositiveInfinity,NegativeInfinity) => Stdlib_Number(NaN)
      case (NegativeInfinity,PositiveInfinity) => Stdlib_Number(NaN)
      case (PositiveInfinity,_) => Stdlib_Number(PositiveInfinity)
      case (_,PositiveInfinity) => Stdlib_Number(NegativeInfinity)
      case (NegativeInfinity,_) => Stdlib_Number(NegativeInfinity)
      case (_,NegativeInfinity) => Stdlib_Number(PositiveInfinity)
      case (DoubleValue(d1),DoubleValue(d2)) => Stdlib_Number(d1-d2)
    }
  }

  private def negate(number:Stdlib_Number):Stdlib_Number = {
    number.value match {
      case NaN => Stdlib_Number(NaN)
      case PositiveInfinity => Stdlib_Number(NegativeInfinity)
      case NegativeInfinity => Stdlib_Number(PositiveInfinity)
      case DoubleValue(0.0) => Stdlib_Number(DoubleValue(0.0),!number.negated)
      case DoubleValue(d1) => Stdlib_Number(DoubleValue(-d1))
    }
  }

  private def addStrings(object1:RTObject,object2:RTObject) = {
    Stdlib_String(object1.toString+object2.toString)
  }

}