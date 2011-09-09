package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class RTUserFunction(val name: Option[RTId], val args: List[RTId], val decl: List[RTId], val source: List[RTExpression]) extends RTFunction {
  var environment: Option[RTEnvironmentRecord] = None

  override def evaluate(env: RTEnvironmentRecord): RTObject = {
    environment = Some(env); this
  }

  override def call(callObject: CallObject): RTObject = {
    val environment = new RTEnvironmentRecord
    for ((id,value) <- args.zip(callObject.args)) {
        println(id + " <- "+value)
      environment.declare(id)
      environment.getReference(id) match {
        case ref: RTReference => ref.setValue(value)
      }
    }
    var retValue: RTObject = Stdlib_Undefined
    for (expr <- source) {
      retValue = expr.evaluate(environment).valueOf
    }
    retValue
  }

  override def toBoolean(): Stdlib_Boolean = {
    Stdlib_Boolean(true)
  }

  override def toString(): String = {
    "function(" + name + "," + args + "," + decl + "," + source + ")"
  }

  override def isObject = true

  override def isPrimitive = false

}

object RTUserFunction {
  def apply(name: Option[RTId], args: List[RTId], decl: List[RTId], source: List[RTExpression]): RTUserFunction = {
    new RTUserFunction(name, args, decl, source)
  }
}