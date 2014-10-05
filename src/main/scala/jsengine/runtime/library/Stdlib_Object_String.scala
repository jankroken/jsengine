package jsengine.runtime.library

import jsengine.runtime.tree.{CallObject, RTObject, RTEnvironmentRecord, RTFunction}

object Stdlib_Object_String extends RTFunction {

  var functionEnvironment: Option[RTEnvironmentRecord] = None

  override def evaluate(env: RTEnvironmentRecord): RTObject = {
    functionEnvironment = Some(env)
    this
  }

  override def booleanValue = Stdlib_Boolean(true)
  override def stringValue = Stdlib_String("String")
  override def isObject = true
  override def isPrimitive = false
  override def typeof = "string"



  override def call(callObject: CallObject): RTObject = {
    val stringValue = callObject.args.length match {
      case 0 => ""
      case _ => callObject.args(0).valueOf.toString
    }
    new Stdlib_String(stringValue)
  }

  override def newCall(callObject: CallObject): RTObject = {
    val stringValue = callObject.args.length match {
      case 0 => ""
      case _ => {
        val Stdlib_String(str,_) = callObject.args(0).valueOf.stringValue
        str
      }
    }
    new Stdlib_String(stringValue,true)
  }

}
