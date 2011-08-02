package jsengine.ast

trait JSStatement extends JSSourceElement

case class JSBlock(statements : List[JSStatement]) extends JSStatement
case class VariableStatement(variableDeclararations : List[VariableDeclaration]) extends JSStatement
case class VariableDeclarations(declarations: List[VariableDeclaration]) extends JSStatement with JSSourceElement
case class VariableDeclaration(name : JSString, initialValue : Option[JSBaseExpression]) extends JSStatement
case class EmptyStatement() extends JSStatement
case class IfStatement(condition: JSBaseExpression, whenTrue: JSStatement, whenFalse: Option[JSStatement]) extends JSStatement 
case class DoWhile (statement: JSStatement, condition: JSBaseExpression) extends JSStatement
case class While (condition: JSBaseExpression, statement: JSStatement) extends JSStatement
case class IterationStatement()
case class ContinueStatement()
case class BreakStatement()
case class ReturnStatement()
case class WithStatement()
case class LabeledStatement()
case class SwitchStatement()
case class ThrowStatement()
case class TryStatement()
case class DebuggerStatement()

