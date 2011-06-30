package jsengine.ast

import jsengine.library.BuiltinObjects

trait JSExpression extends JSSourceElement {
  def evaluate:JSObject
}