package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_GreaterThan extends RTFunction {

    def call(callObject: CallObject):RTObject = {
        val val1 = callObject.args(0)
        val val2 = callObject.args(1)
        (val1,val2) match {
            case (num1:Stdlib_Number,num2:Stdlib_Number) => compareNumbers(num1,num2)
            case _ => throw new RuntimeException("Not implemented: +("+val1+","+val2+")")
        }
    }
  	def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
    override def toString = "lib_>"

    private def compareNumbers(number1:Stdlib_Number, number2: Stdlib_Number) = {
        (number1.value,number2.value) match {
            case (NaN,_) => Stdlib_Boolean(false)
            case (_,NaN) => Stdlib_Boolean(false)
            case (PositiveInfinity,NegativeInfinity) => Stdlib_Boolean(true)
            case (NegativeInfinity,PositiveInfinity) => Stdlib_Boolean(false)
            case (PositiveInfinity,_) => Stdlib_Boolean(true)
            case (_,PositiveInfinity) => Stdlib_Boolean(false)
            case (NegativeInfinity,_) => Stdlib_Boolean(false)
            case (_,NegativeInfinity) => Stdlib_Boolean(true)
            case (DoubleValue(d1),DoubleValue(d2)) => Stdlib_Boolean(d1>d2)
        }
    }

}