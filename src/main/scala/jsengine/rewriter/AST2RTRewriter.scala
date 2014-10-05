package jsengine.rewriter

import jsengine.ast._
import jsengine.runtime.tree._
import jsengine.runtime.library._

object AST2RTRewriter {

  private def rewriteOptionExpression(optExpr: Option[JSBaseExpression]):RTExpression = optExpr match {
    case None => Stdlib_Undefined
    case Some(expr) => rewriteExpression(expr)
  }

  private def rewriteOptionStatement(optStatement: Option[JSStatement]):Option[RTExpression] = optStatement match {
    case None => None
    case Some(statement) => Some(rewriteStatement(statement))
  }

  private def rewriteStatementList(statements: List[JSStatement]):List[RTExpression] = statements match {
    case List() => List()
    case statement :: tail => rewriteStatement(statement) :: rewriteStatementList(tail)
  }

  private def rewriteExpressionList(exprList: List[JSBaseExpression]):List[RTExpression] = exprList match {
    case List() => List()
    case expr :: tail => rewriteExpression(expr) :: rewriteExpressionList(tail)
  }

  private def rewriteId(id: JSIdentifier):RTId =  RTId(id.value)

  private def rewriteOptionId(optionalId: Option[JSIdentifier]):Option[RTId] = optionalId match {
    case None => None
    case Some(id) => Some(rewriteId(id))
  }

  private def rewriteExpression (expression: JSBaseExpression): RTExpression = expression match {
    case OperatorCall (operator, args) => Stdlib_Undefined
    case BuiltIn("&&") => Stdlib_Operator_LogicalAnd
    case BuiltIn("+") => Stdlib_Operator_Plus
    case BuiltIn("-") => Stdlib_Operator_Minus
    case BuiltIn(">") => Stdlib_Operator_GreaterThan
    case BuiltIn("<") => Stdlib_Operator_LessThan
    case BuiltIn("*") => Stdlib_Operator_Multiply
    case BuiltIn("array") => Stdlib_Object_Array
    case BuiltIn("typeof") => Stdlib_Operator_Typeof
    case BuiltIn("void") => Stdlib_Operator_Void
    case BuiltIn("===") => Stdlib_Operator_Equals3
    case BuiltIn("==") => Stdlib_Operator_Equals2
    case BuiltIn("!==") => Stdlib_Operator_NotEquals3
    case BuiltIn("!=") => Stdlib_Operator_NotEquals2
    case BuiltIn("<=") => Stdlib_Operator_LessThanOrEquals2
    case BuiltIn("<==") => Stdlib_Operator_LessThanOrEquals3
    case BuiltIn(">=") => Stdlib_Operator_GreaterThanOrEquals2
    case BuiltIn(">==") => Stdlib_Operator_GreaterThanOrEquals3
    case BuiltIn("||") => Stdlib_Operator_LogicalOr
    case BuiltIn("/") => Stdlib_Operator_Divide
    case JSExpression(expressions) => new RTBlock(rewriteExpressionList(expressions))
    case ConditionalExpression(condition, trueExpression, falseExpression) => Stdlib_Undefined
    case Lookup(expr,index) => RTLookup(rewriteExpression(expr),rewriteExpression(index))
    case New(function,args) => RTNewCall(rewriteExpression(function),rewriteExpressionList(args))
    case Call(function,args) => RTSimpleFunctionCall(rewriteExpression(function),rewriteExpressionList(args))
    case Assign(left,value) => RTAssign(rewriteExpression(left),rewriteExpression(value))
    case PostfixExpression(expression,Operator("--")) => Stdlib_Undefined
    case PostfixExpression(expression,Operator("++")) => Stdlib_Undefined
    case JSFunction(functionName,arguments, source)  => Stdlib_Undefined
    case JSFunctionExpression(name,args,decl,source) => {
      RTUserFunction(rewriteOptionId(name),args.map(rewriteId),decl.map(rewriteId),rewriteStatementList(source))
    }
    case JSBoolean(value) => Stdlib_Undefined
    case JSIdentifier("undefined") => Stdlib_Undefined
    case JSIdentifier("true") => Stdlib_Boolean(true)
    case JSIdentifier("false") => Stdlib_Boolean(false)
    case JSIdentifier("NaN") => Stdlib_Number(NaN)
    case JSIdentifier("Infinity") => Stdlib_Number(PositiveInfinity)
    case JSIdentifier("null") => Stdlib_Null
    case JSIdentifier(value) => RTGetReferenceById(RTId(value))
    case JSNativeCall(identifier) => Stdlib_Undefined
    case JSNumber(value) => Stdlib_Number(value)
    case JSString(value) => Stdlib_String(value)
    case JSRegexLiteral(value) => Stdlib_Undefined
  }

  private def rewriteStatement (statement: JSStatement) : RTExpression = statement match {
    case Declare(JSIdentifier(id)) => RTDeclare(RTId(id))
    case EmptyStatement() => Stdlib_Undefined
    case JSBlock(statements) => new RTBlock(rewriteStatementList(statements))
    case IfStatement(condition, whenTrue, whenFalse) => {
        RTCond(rewriteExpression(condition),
               rewriteStatement(whenTrue),
               rewriteOptionStatement(whenFalse).getOrElse(Stdlib_Undefined))
    }
    case DoWhile (statement, condition)  => Stdlib_Undefined
    case While (condition, statement) => Stdlib_Undefined
    case ContinueStatement(label) => Stdlib_Undefined
    case ForIn(init,expr,source) => Stdlib_Undefined
    case BreakStatement(label) => Stdlib_Undefined
    case ReturnStatement(None) => RTReturn(Stdlib_Undefined)
    case ReturnStatement(Some(value))  => RTReturn(rewriteExpression(value))
    case WithStatement(expr, statement) => Stdlib_Undefined
    case SwitchStatement(expr, cases) => Stdlib_Undefined
    case LabeledStatement(label, statement) => Stdlib_Undefined
    case ThrowStatement(expr) => new Stdlib_Throw(rewriteExpression(expr))
    case Try(statement, Some(Catch(id,catchStatement)), finallyStatement) => new RTTry()
    case Try(statement, None, finallyStatement) => new RTTry()
    case DebuggerStatement() => Stdlib_Undefined
    case expr:JSBaseExpression => rewriteExpression(expr)
    case unhandledStatement => throw new RuntimeException("Implementation error: missing handling of AST node: "+unhandledStatement)
  }

  def rewriteSource (source: JSSource) : RTSource = {
    println("RT: rewriting "+source)
    source match {
      case JSSource(statements) => RTSource(rewriteStatementList(statements))
    }
  }

}