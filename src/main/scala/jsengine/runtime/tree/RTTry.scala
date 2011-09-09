package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

class RTTry() extends RTExpression {
  def evaluate(env: RTEnvironmentRecord): RTObject = {
    throw new RuntimeException("Not implemented")
  }

}