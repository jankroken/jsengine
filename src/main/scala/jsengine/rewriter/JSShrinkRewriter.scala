package jsengine.rewriter

import jsengine.ast._

object JSShrinkRewriter {

    private def rewriteOptionExpression(optExpr: Option[JSBaseExpression]):Option[JSBaseExpression] = {
        optExpr match {
			case None => None
			case Some(expr) => Some(rewriteExpression(expr))
        }
    }
  
    private def rewriteOptionStatement(optStatement: Option[JSStatement]):Option[JSStatement] = {
        optStatement match {
			case None => None
			case Some(statement) => Some(rewriteStatement(statement))
        }
    }

    private def optionToUndefined (optExpr: Option[JSBaseExpression]): JSBaseExpression = {
        optExpr match {
			case None => JSIdentifier("undefined")
			case Some(expr) => expr
        }
    }

    private def rewriteStatementList(statements: List[JSStatement]):List[JSStatement] = {
        statements match {
            case List() => List()
            case statement :: tail => rewriteStatement(statement) :: rewriteStatementList(tail)
        }
    }
    
    private def rewriteExpressionList(exprList: List[JSBaseExpression]):List[JSBaseExpression] = {
        exprList match {
            case List() => List()
            case expr :: tail => rewriteExpression(expr) :: rewriteExpressionList(tail)
        }
    }

    private def rewriteExpression (expression: JSBaseExpression): JSBaseExpression = {
			expression match {
			  case OperatorCall (operator, args) => OperatorCall(operator, rewriteExpressionList(args))
			  case BuiltIn(name) => expression
			  case JSExpression(expressions) => JSExpression(expressions.map(rewriteExpression))
			  case ConditionalExpression(condition, trueExpression, falseExpression) => {
				  ConditionalExpression(rewriteExpression(condition),
				  						rewriteExpression(trueExpression),
				  						rewriteExpression(falseExpression))
			  }
			  case Lookup(expr,index) => Lookup(rewriteExpression(expr),rewriteExpression(index))
			  case New(function,args) => New(rewriteExpression(function),args.map(rewriteExpression))
			  case Call(function,args) => Call(rewriteExpression(function),args.map(rewriteExpression))
			  case Assign(left,value) => Assign(rewriteExpression(left), rewriteExpression(value))
			  case PostfixExpression(expression,Operator("--")) => Call(BuiltIn(")--"),List(expression))
			  case PostfixExpression(expression,Operator("++")) => Call(BuiltIn(")++"),List(expression))
			  case JSArrayLiteral(elements) => Call(BuiltIn("array"),elements.map((oe) => rewriteExpression(optionToUndefined(oe))))
			  case JSFunction(functionName,arguments, source)  => {
			     JSFunction(functionName,arguments,rewriteStatementList(source))
			  }
			  case JSFunctionExpression(name,args,decl,source) => {
				  JSFunctionExpression(name,args,decl,rewriteStatementList(source))
			  }
			  case JSBoolean(value) => JSBoolean(value)
			  case JSIdentifier(value) => JSIdentifier(value)
			  case JSNativeCall(identifier) => JSNativeCall(identifier)
			  case JSNumber(value) => JSNumber(value) 
			  case JSString(value) => JSString(value) 
			  case JSRegexLiteral(value) => JSRegexLiteral(value)
			}
    }
  
    private def rewriteStatement (statement: JSStatement) : JSStatement = statement match {
      case Declare(identifier) => Declare(identifier)
      case JSBlock(List()) => EmptyStatement()
      case JSBlock(List(statement)) => rewriteStatement(statement)
      case JSBlock(a::b::tail) => JSBlock(rewriteStatementList(a::b::tail))
			case EmptyStatement() => EmptyStatement()
			case IfStatement(condition, whenTrue, whenFalse) => IfStatement(rewriteExpression(condition),
                       rewriteStatement(whenTrue),
                       rewriteOptionStatement(whenFalse))
			case DoWhile (statement, condition)  => DoWhile(rewriteStatement(statement),rewriteExpression(condition))
			case While (condition, statement) => While(rewriteExpression(condition),rewriteStatement(statement))
			case ContinueStatement(label) => statement 
			case ForIn(init,expr,source) => ForIn(rewriteStatement(init), rewriteExpression(expr), rewriteStatement(source))
			case BreakStatement(label) => statement
			case ReturnStatement(None) => statement
			case ReturnStatement(Some(value))  => ReturnStatement(Some(rewriteExpression(value)))
			case WithStatement(expr, statement) => WithStatement(rewriteExpression(expr),rewriteStatement(statement))
      case SwitchStatement(expr, cases)  =>  {
				def rewriteCase(caseClause: CaseClause): CaseClause = caseClause match {
          case LabeledCaseClause(label,statements) =>
            LabeledCaseClause(label,rewriteStatementList(statements))
          case DefaultClause(statements) =>
            DefaultClause(rewriteStatementList(statements))
        }
        SwitchStatement(rewriteExpression(expr),cases.map(rewriteCase))
      }
      case LabeledStatement(label, statement) => LabeledStatement(label, rewriteStatement(statement))
			case ThrowStatement(expr) => ThrowStatement(rewriteExpression(expr))
			case Try(statement, Some(Catch(id,catchStatement)), finallyStatement) => Try(rewriteStatement(statement),
               Some(Catch(id,rewriteStatement(catchStatement))),
               rewriteOptionStatement(finallyStatement))
			case Try(statement, None, finallyStatement) => Try(rewriteStatement(statement),
               None,
               rewriteOptionStatement(finallyStatement))
			case DebuggerStatement() => statement
			case expr:JSBaseExpression => rewriteExpression(expr)
			case unhandledStatement => throw new RuntimeException("Implementation error: missing handling of AST node: "+unhandledStatement)
    }
  
    def rewriteSource (source: JSSource) = JSSource(rewriteStatementList(source.sourceElements))

}