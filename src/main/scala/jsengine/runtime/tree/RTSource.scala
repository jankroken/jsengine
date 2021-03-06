package jsengine.runtime.tree

import jsengine.runtime.library._

class RTSource(val statements: List[RTExpression]) {
  def evaluate(env: RTEnvironmentRecord):RTObject = {
    var retValue: RTObject = Stdlib_Undefined
    for (statement <- statements) {
      retValue = statement.evaluate(env).valueOf
    }
    retValue
  }

  override def toString = s"<source: $statements>"
}

object RTSource {
  def apply(statements : List[RTExpression]) = new RTSource(statements)
}