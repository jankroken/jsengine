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
case class For(init: ForInit, update: ForUpdate, body: JSStatement) extends JSStatement
case class ForInit(init: JSStatement)
sealed trait ForUpdate
case class ForInUpdate(statement: JSBaseExpression) extends ForUpdate
case class ForSemicolonUpdate(test: Option[JSBaseExpression], update: Option[JSStatement]) extends ForUpdate
case class ContinueStatement(label: Option[JSString]) extends JSStatement
case class BreakStatement(label: Option[JSString]) extends JSStatement
case class ReturnStatement(value: Option[JSBaseExpression]) extends JSStatement
case class WithStatement(expr: JSBaseExpression, statement: JSStatement) extends JSStatement
// case class LabeledStatement()
case class SwitchStatement(expr: JSBaseExpression, cases: List[CaseClause]) extends JSStatement
sealed trait CaseClause
case class LabeledCaseClause(label: JSBaseExpression, statements: List[JSStatement]) extends CaseClause
case class DefaultClause(statements: List[JSStatement]) extends CaseClause

case class ThrowStatement()
case class TryStatement()
case class DebuggerStatement()

