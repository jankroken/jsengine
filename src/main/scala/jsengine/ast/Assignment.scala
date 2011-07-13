package jsengine.ast

sealed trait Assignment
case class VariableAssignment(val variableName: JSString, val initialValue: JSExpression) extends Assignment with JSExpression
case class VariableDeclaration(val variableName: JSString, val initialValue: Option[JSExpression]) extends Assignment with JSSourceElement
case class VariableDeclarations(val declarations: List[VariableDeclaration]) extends JSSourceElement
