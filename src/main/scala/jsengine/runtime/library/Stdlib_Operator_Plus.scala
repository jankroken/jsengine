package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Plus extends RTFunction {

  def call(callObject: CallObject):RTObject = {
    if (callObject.args.length == 1) {
      callObject.args(0)
    } else {
      val val1 = callObject.args(0)
      val val2 = callObject.args(1)
      (val1,val2) match {
        case (num1:Stdlib_Number,num2:Stdlib_Number) => addNumbers(num1,num2)
        case (str1:Stdlib_String,o2:RTObject) => addStrings(str1,o2)
        case (o1:RTObject,str2:Stdlib_String) => addStrings(o1,str2)
        case _ => throw new RuntimeException("Not implemented: +("+val1+","+val2+")")
      }
    }
  }
  def booleanValue: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
  override def stringValue = Stdlib_String("internal:lib_+")
  override def toString = "lib_+"

  private def addNumbers(number1:Stdlib_Number, number2: Stdlib_Number) = {
    (number1.value,number2.value) match {
      case (NaN,_) => Stdlib_Number(NaN)
      case (_,NaN) => Stdlib_Number(NaN)
      case (PositiveInfinity,NegativeInfinity) => Stdlib_Number(NaN)
      case (NegativeInfinity,PositiveInfinity) => Stdlib_Number(NaN)
      case (PositiveInfinity,_) => Stdlib_Number(PositiveInfinity)
      case (_,PositiveInfinity) => Stdlib_Number(PositiveInfinity)
      case (NegativeInfinity,_) => Stdlib_Number(NegativeInfinity)
      case (_,NegativeInfinity) => Stdlib_Number(NegativeInfinity)
      case (DoubleValue(d1),DoubleValue(d2)) => Stdlib_Number(d1+d2)
    }
  }

  private def addStrings(object1:RTObject,object2:RTObject) = {
      Stdlib_String(object1.toString+object2.toString)
  }

}