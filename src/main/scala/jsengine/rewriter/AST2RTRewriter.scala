package jsengine.rewriter

import jsengine.ast._
import jsengine.runtime.tree._
import jsengine.runtime.types._
import jsengine.runtime.library._

object AST2RTRewriter {


    private def rewriteOptionExpression(optExpr: Option[JSBaseExpression]):RTExpression = {
        optExpr match {
			case None => Stdlib_Undefined
			case Some(expr) => rewriteExpression(expr)
        }
    }
  
    private def rewriteOptionStatement(optStatement: Option[JSStatement]):Option[RTExpression] = {
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

    private def rewriteStatementList(statements: List[JSStatement]):List[RTExpression] = {
        statements match {
            case List() => List()
            case statement :: tail => rewriteStatement(statement) :: rewriteStatementList(tail)
        }
    }
    
    private def rewriteExpressionList(exprList: List[JSBaseExpression]):List[RTExpression] = {
        exprList match {
            case List() => List()
            case expr :: tail => rewriteExpression(expr) :: rewriteExpressionList(tail)
        }
    }
    
    private def rewriteId(id: JSIdentifier):RTId = {
    	id match { case JSIdentifier(foo) => RTId(foo) } 
    }
    
    private def rewriteOptionId(optionalId: Option[JSIdentifier]):Option[RTId] = {
    	optionalId match {
    		case None => None
    		case Some(id) => Some(rewriteId(id))
    	}
    }

    private def rewriteExpression (expression: JSBaseExpression): RTExpression = {
		expression match {
			  case OperatorCall (operator, args) => Stdlib_Undefined
			  case BuiltIn("&&") => Stdlib_Operator_BooleanAnd
			  case JSExpression(expressions) => new RTBlock(rewriteExpressionList(expressions))
			  case ConditionalExpression(condition, trueExpression, falseExpression) => Stdlib_Undefined
			  case Lookup(expr,index) => Stdlib_Undefined
			  case New(function,args) => Stdlib_Undefined
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
			  case JSIdentifier(value) => RTGetReferenceById(RTId(value))
			  case JSNativeCall(identifier) => Stdlib_Undefined
			  case JSNumber(value) => Stdlib_Number(value) 
			  case JSString(value) => Stdlib_String(value) 
			  case JSRegexLiteral(value) => Stdlib_Undefined
			}
    }
  
    private def rewriteStatement (statement: JSStatement) : RTExpression = {
    	statement match {
    	    case Declare(JSIdentifier(id)) => RTDeclare(RTId(id))
    	    case EmptyStatement() => Stdlib_Undefined
    	    case JSBlock(statements) => new RTBlock(rewriteStatementList(statements))
			case IfStatement(condition, whenTrue, whenFalse) => Stdlib_Undefined
			case DoWhile (statement, condition)  => Stdlib_Undefined
			case While (condition, statement) => Stdlib_Undefined
			case ContinueStatement(label) => Stdlib_Undefined 
			case ForIn(init,expr,source) => Stdlib_Undefined
			case BreakStatement(label) => Stdlib_Undefined
			case ReturnStatement(None) => Stdlib_Undefined
			case ReturnStatement(Some(value))  => Stdlib_Undefined
			case WithStatement(expr, statement) => Stdlib_Undefined
		    case SwitchStatement(expr, cases) => Stdlib_Undefined
			case LabeledStatement(label, statement) => Stdlib_Undefined
			case ThrowStatement(expr) => new Stdlib_Throw(rewriteExpression(expr))
			case Try(statement, Some(Catch(id,catchStatement)), finallyStatement) => new RTTry()
			case Try(statement, None, finallyStatement) => new RTTry();																		
			case DebuggerStatement() => Stdlib_Undefined;
			case expr:JSBaseExpression => rewriteExpression(expr)
			case unhandledStatement => throw new RuntimeException("Implementation error: missing handling of AST node: "+unhandledStatement)
    	}
    }
  
    def rewriteSource (source: JSSource) : RTSource = {
      println("RT: rewriting "+source)
    	source match {
    	  case JSSource(statements) => RTSource(rewriteStatementList(statements))
    	}
    }
        
}