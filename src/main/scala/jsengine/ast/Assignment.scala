package jsengine.ast

sealed trait Assignment
case class VariableAssignment(val variableName: JSString, val initialValue: JSExpression) extends Assignment with JSExpression  {
  def evaluate:JSObject = {
    throw new RuntimeException("TODO")
  }
}
case class VariableDeclaration(val variableName: JSString, val initialValue: JSExpression) extends Assignment with JSSourceElement {
  def evaluate:JSObject = {
    throw new RuntimeException("TODO")
  }
}
case class VariableDeclarations(val declarations: List[VariableDeclaration]) extends JSSourceElement {
  def evaluate:JSObject = {
    throw new RuntimeException("Internal error: Variable declarations should be split/expanded before evaluation")
  }
}