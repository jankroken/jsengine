package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Object_Array extends RTFunction {

  var functionEnvironment:Option[RTEnvironmentRecord] = None

  override def booleanValue = Stdlib_Boolean(true)
  override def stringValue = Stdlib_String("function Array() { <native code> }")
  override def isObject = true
  override def isPrimitive = false


  override def evaluate(env: RTEnvironmentRecord): RTObject = {
    functionEnvironment = Some(env);
    this
  }

  override def call(callObject: CallObject): RTObject = {
    val Some(env) = functionEnvironment
    val array = RTArray()
    val environment = new RTEnvironmentRecord(functionEnvironment)
    for ((expr, index) <- callObject.args.zipWithIndex) {
      println("Array.initialize: "+index+" -> "+expr)
      val value = expr.evaluate(env).valueOf
      array.setProperty(Stdlib_Number(DoubleValue(index)),value)
    }
    var retValue: RTObject = Stdlib_Undefined
    array
  }

  override def newCall(callObject: CallObject): RTObject = {
    val Some(env) = functionEnvironment
    val array = RTArray()
    val environment = new RTEnvironmentRecord(functionEnvironment)

    for ((expr, index) <- callObject.args.zipWithIndex) {
      println("Array.initialize: "+index+" -> "+expr)
      val value = expr.evaluate(env).valueOf
      array.setProperty(Stdlib_Number(DoubleValue(index)),value)
    }
    array
  }

  override def toString:String = "Array"

}
