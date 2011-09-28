package jsengine.runtime.library

import jsengine.runtime.tree._
import jsengine.runtime.library.Stdlib_Boolean

object Stdlib_Operator_Equals3 extends RTFunction {

    def call(callObject: CallObject):RTObject = {
        val val1 = callObject.args(0)
        val val2 = callObject.args(1)
        val result = (val1,val2) match {
          case (Stdlib_Undefined,Stdlib_Undefined) => true
          case (Stdlib_Undefined,_) => false
          case (Stdlib_Boolean(true),Stdlib_Boolean(true)) => true
          case (Stdlib_Boolean(false),Stdlib_Boolean(false)) => true
          case (Stdlib_Boolean(_),_) => false
          case (Stdlib_String(s1),Stdlib_String(s2)) => (s1 == s2)
          case (Stdlib_String(_),_) => false
        }
      return Stdlib_Boolean(result)
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