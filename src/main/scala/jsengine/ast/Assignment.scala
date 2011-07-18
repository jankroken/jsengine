package jsengine.ast

sealed trait Assignment
case class VariableAssignment(val variableName: JSString, val initialValue: JSBaseExpression) extends Assignment with JSBaseExpression
case class VariableDeclaration(val variableName: JSString, val initialValue: Option[JSBaseExpression]) extends Assignment with JSSourceElement
case class VariableDeclarations(val declarations: List[VariableDeclaration]) extends JSSourceElement
