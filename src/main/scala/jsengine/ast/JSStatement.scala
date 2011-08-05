package jsengine.ast

trait JSStatement extends ASTNode

case class JSBlock(statements : List[JSStatement]) extends JSStatement
case class VariableDeclarations(declarations: List[VariableDeclaration]) extends JSStatement	// before rewrite
case class VariableDeclaration(name : JSIdentifier, initialValue : Option[JSBaseExpression]) extends JSStatement // before rewrite
case class EmptyStatement() extends JSStatement
case class IfStatement(condition: JSBaseExpression, whenTrue: JSStatement, whenFalse: Option[JSStatement]) extends JSStatement 
case class DoWhile (statement: JSStatement, condition: JSBaseExpression) extends JSStatement
case class While (condition: JSBaseExpression, statement: JSStatement) extends JSStatement
case class ForStatement(init: Option[ForInit], update: ForUpdate, body: JSStatement) extends JSStatement // before rewrite
case class ForInit(init: JSStatement) // before rewrite 
sealed trait ForUpdate // before rewrite 
case class ForInUpdate(statement: JSBaseExpression) extends ForUpdate // before rewrite
case class ForSemicolonUpdate(test: Option[JSBaseExpression], update: Option[JSStatement]) extends ForUpdate // before update
case class ContinueStatement(label: Option[JSIdentifier]) extends JSStatement
case class BreakStatement(label: Option[JSIdentifier]) extends JSStatement
case class ReturnStatement(value: Option[JSBaseExpression]) extends JSStatement
case class WithStatement(expr: JSBaseExpression, statement: JSStatement) extends JSStatement
// case class LabeledStatement()
case class SwitchStatement(expr: JSBaseExpression, cases: List[CaseClause]) extends JSStatement
sealed trait CaseClause
case class LabeledCaseClause(label: JSBaseExpression, statements: List[JSStatement]) extends CaseClause
case class DefaultClause(statements: List[JSStatement]) extends CaseClause
case class LabeledStatement(label: JSIdentifier, statement: JSStatement) extends JSStatement

case class ThrowStatement(expr: JSBaseExpression) extends JSStatement
case class TryStatement(block: JSBlock, tryTail: TryTail) extends JSStatement
case class TryTail(id: Option[JSIdentifier], catchBlock: Option[JSBlock], finallyBlock: Option[JSBlock])
case class DebuggerStatement() extends JSStatement

// After rewrite

case class Declare(id: JSIdentifier) extends JSStatement
case class ForIn(id: JSStatement, expr: JSBaseExpression, body: JSStatement) extends JSStatement
case class Try (tryStatement: JSStatement, catchBlock: Option[Catch], finallyBlock: Option[JSStatement]) extends JSStatement
case class Catch(id: JSIdentifier, statement: JSStatement)
