package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Operator_Plus extends RTFunction {

    def call(callObject: CallObject):RTObject = {
        val val1 = callObject.args(0)
        val val2 = callObject.args(1)
        (val1,val2) match {
            case (num1:Stdlib_Number,num2:Stdlib_Number) => addNumbers(num1,num2)
            case (str1:Stdlib_String,o2:RTObject) => addStrings(str1,o2)
            case (o1:RTObject,str2:Stdlib_String) => addStrings(o1,str2)
            case _ => throw new RuntimeException("Not implemented: +("+val1+","+val2+")")
        }
    }
  	def toBoolean: Stdlib_Boolean = { throw new RuntimeException("not implemented") }
    override def toString = "lib_+"

    private def addNumbers(number1:Stdlib_Number, number2: Stdlib_Number) = {
        Stdlib_Number(number1.nativeDoubleValue+number2.nativeDoubleValue)
    }

    private def addStrings(object1:RTObject,object2:RTObject) = {
        Stdlib_String(object1.toString+object2.toString)
    }
      
}