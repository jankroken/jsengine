package jsengine.runtime.tree

class RTReturn(expr: RTExpression) extends RTExpression {
  def evaluate(env: RTEnvironmentRecord): RTObject = {
    throw new RTReturnException(expr.evaluate(env))
  }
}

object RTReturn {
    def apply(expr: RTExpression) = {
        new RTReturn(expr)
    }
}