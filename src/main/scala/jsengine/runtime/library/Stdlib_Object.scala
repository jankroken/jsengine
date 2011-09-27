package jsengine.runtime.library

import jsengine.runtime.tree._

object Stdlib_Object extends RTFunction {

  var functionEnvironment: Option[RTEnvironmentRecord] = None

  override def evaluate(env: RTEnvironmentRecord): RTObject = {
    functionEnvironment = Some(env);
    this
  }

  override def toBoolean = Stdlib_Boolean(true)
  override def isObject = true
  override def isPrimitive = false
  override def toString = "Object"
  override def typeof = "object"

  override def call(callObject: CallObject): RTObject = {
    val baseValue = if(callObject.args.length == 0) None else Some(callObject.args(0))
    val Some(env) = functionEnvironment
    val environment = new RTEnvironmentRecord(functionEnvironment)
    new RTUserObject(this,baseValue)
  }

  override def newCall(callObject: CallObject): RTObject = {
    val baseValue = if(callObject.args.length == 0) None else Some(callObject.args(0))
    val Some(env) = functionEnvironment
    val environment = new RTEnvironmentRecord(functionEnvironment)
    new RTUserObject(this,baseValue)
  }

}
