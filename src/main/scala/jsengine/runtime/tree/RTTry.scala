package jsengine.runtime.tree

class RTTry() extends RTExpression {
  def evaluate(env: RTEnvironmentRecord): RTObject = {
    throw new RuntimeException("Not implemented")
  }

}