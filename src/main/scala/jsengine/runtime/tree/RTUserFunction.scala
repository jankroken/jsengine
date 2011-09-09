package jsengine.runtime.tree

import jsengine.runtime.library._

class RTUserFunction(val name: Option[RTId], val args: List[RTId], val decl: List[RTId], val source: List[RTExpression]) extends RTFunction {
  var functionEnvironment: Option[RTEnvironmentRecord] = None

  override def evaluate(env: RTEnvironmentRecord): RTObject = {
    functionEnvironment = Some(env);
    this
  }

  override def call(callObject: CallObject): RTObject = {
    val environment = new RTEnvironmentRecord(functionEnvironment)
    for ((id, value) <- args.zip(callObject.args)) {
      println(id + " <- " + value)
      environment.declare(id)
      environment.getReference(id) match {
        case ref: RTReference => ref.setValue(value)
      }
    }
    decl.map(environment.declare(_))
    //    for (id <- decl) {
    //      environment.declare(id)
    //    }
    var retValue: RTObject = Stdlib_Undefined
    try {
        for (expr <- source) {
            expr.evaluate(environment).valueOf
        }
    } catch {
      case ret: RTReturnException => {
        retValue = ret.value
      }
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