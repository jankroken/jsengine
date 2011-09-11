package jsengine.rewriter

import jsengine.ast._

object JSCallRewriter {

    private def rewriteOptionExpression(optExpr: Option[JSBaseExpression]):Option[JSBaseExpression] = {
        optExpr match {
			case None => None
			case Some(expr) => Some(rewriteExpression(expr))
        }
    }
  
    private def optionToUndefined (optExpr: Option[JSBaseExpression]): JSBaseExpression = {
        optExpr match {
			case None => JSIdentifier("undefined")
			case Some(expr) => expr
        }
    }

    private def literalObjectToFunctionCall(o: JSLiteralObject): New = {
    	def toCode(property: (JSExpression, JSExpression)):Assign = {
    		property match {
    		  	case (name,value) => Assign(name,value)
    		}
    	}
    	val properties = o match { case JSLiteralObject(properties) => properties }
    	val source = properties.map((property) => property match {
        case (name,value) => Assign(Lookup(JSIdentifier("this"),name),value)
      })
    	val function = JSFunction(None,List(),source)
    	New(function,List())
    }
    
    private def rewriteStatementList(statements: List[JSStatement]):List[JSStatement] = {
        statements match {
            case List() => List()
            case statement :: tail => rewriteStatement(statement) ::: rewriteStatementList(tail)
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
			  case JSExpression(expressions) => JSExpression(expressions.map(rewriteExpression _))
			  case ConditionalExpression(condition, trueExpression, falseExpression) => {
				  ConditionalExpression(rewriteExpression(condition),
				  						rewriteExpression(trueExpression),
				  						rewriteExpression(falseExpression))
			  }
			  case OperatorCall(Operator(name), args: List[JSBaseExpression]) => { // only to handle recursion in bottom-up replacement
				  Call(BuiltIn(name),rewriteExpressionList(args))
			  }
			  case Lookup(expr,index) => Lookup(rewriteExpression(expr),rewriteExpression(index))
			  case New(function,args) => New(rewriteExpression(function),args.map(rewriteExpression _))
			  case Call(function,args) => Call(rewriteExpression(function),args.map(rewriteExpression _))
			  case Assign(left,value) => Assign(rewriteExpression(left), rewriteExpression(value))
			  case PostfixExpression(expression,Operator("--")) => Call(BuiltIn(")--"),List(expression))
			  case PostfixExpression(expression,Operator("++")) => Call(BuiltIn(")++"),List(expression))
			  case JSArrayLiteral(elements) => Call(BuiltIn("array"),elements.map((oe) => rewriteExpression(optionToUndefined(oe))))
			  case JSFunction(functionName,arguments, source)  => {
			     JSFunction(functionName,arguments,rewriteStatementList(source))
			  }
			  case JSBoolean(value) => JSBoolean(value)
			  case JSIdentifier(value) => JSIdentifier(value)
			  case JSNativeCall(identifier) => JSNativeCall(identifier)
			  case literalObject@JSLiteralObject(properties) => literalObjectToFunctionCall(literalObject)
			  case JSNumber(value) => JSNumber(value) 
			  case JSString(value) => JSString(value) 
			  case JSRegexLiteral(value) => JSRegexLiteral(value)
			}
    }
  
    private def rewriteStatement (statement: JSStatement) : List[JSStatement] = {
    	statement match {
    	    case Declare(identifier) => List(Declare(identifier))
			case JSBlock(statements) => List(JSBlock(rewriteStatementList(statements)))
			case VariableDeclarations(declarations) =>  (rewriteStatementList(declarations))
			case VariableDeclaration(name, None) => Declare(name) :: List() 
			case VariableDeclaration(name, Some(initialValue)) => Declare(name) :: Assign(name,rewriteExpression(initialValue)) :: List()
			case EmptyStatement() => List(EmptyStatement())
			case IfStatement(condition, whenTrue, None) => List(IfStatement(rewriteExpression(condition),
			  																	  JSBlock(rewriteStatement(whenTrue)),
					  																	  None))
			case IfStatement(condition, whenTrue, Some(whenFalse)) => List(IfStatement(rewriteExpression(condition),
			  																	  JSBlock(rewriteStatement(whenTrue)),
			  																	  Some(JSBlock(rewriteStatement(whenFalse)))))
			case DoWhile (statement, condition)  => List(DoWhile(JSBlock(rewriteStatement(statement)),rewriteExpression(condition)))
			case While (condition, statement) => List(While(rewriteExpression(condition),JSBlock(rewriteStatement(statement))))
			case ContinueStatement(label) => List(statement) 
			case forIn @ ForIn(init,expr,source) => {
			    forIn match {
			    	case ForIn(JSBlock(List(Declare(y))),expr,source) => List(Declare(y),ForIn(y,expr,source))
            case _ => throw new RuntimeException("should not happen")
			    }
			}
			case BreakStatement(label) => List(statement)
			case ReturnStatement(None) => List(statement)
			case ReturnStatement(Some(value))  => List(ReturnStatement(Some(rewriteExpression(value))))
			case WithStatement(expr, statement) => List(WithStatement(rewriteExpression(expr),JSBlock(rewriteStatement(statement))))
		    case SwitchStatement(expr, cases)  =>  {
				def rewriteCase(caseClause: CaseClause): CaseClause = {
				    caseClause match {
				        case LabeledCaseClause(label,statements) => 
				            LabeledCaseClause(label,rewriteStatementList(statements))
				        case DefaultClause(statements) => 
				            DefaultClause(rewriteStatementList(statements))
				        }
			        }
				    List(SwitchStatement(rewriteExpression(expr),cases.map(rewriteCase _)))
			    }
			case LabeledStatement(label, statement) => List(LabeledStatement(label, JSBlock(rewriteStatement(statement))))
			case ThrowStatement(expr) => List(ThrowStatement(rewriteExpression(expr)))
			case TryStatement(block, TryTail(None,None,Some(finallyBlock))) => {
				List(Try(JSBlock(rewriteStatement(block)),None,Some(JSBlock(rewriteStatement(finallyBlock)))))
			}
			case TryStatement(block, TryTail(Some(id),Some(catchBlock),None)) => {
				List(Try(JSBlock(rewriteStatement(block)),Some(Catch(id,JSBlock(rewriteStatement(catchBlock)))),None)) 
			}
			case TryStatement(block, TryTail(Some(id),Some(catchBlock),Some(finallyStatement))) => {
				List(Try(JSBlock(rewriteStatement(block)),
				         Some(Catch(id,JSBlock(rewriteStatement(catchBlock)))),
				         Some(JSBlock(rewriteStatement(finallyStatement))))) 
			}
			case DebuggerStatement() => List(statement)
			case expr:JSBaseExpression => List(rewriteExpression(expr))
			case unhandledStatement => throw new RuntimeException("Implementation error: missing handling of AST node: "+unhandledStatement)
    	}
    }
  
    def rewriteSource (source: JSSource) : JSSource = {
    	source match {
    	  case JSSource(statements) => JSSource(rewriteStatementList(statements))
    	}
    }
  
}