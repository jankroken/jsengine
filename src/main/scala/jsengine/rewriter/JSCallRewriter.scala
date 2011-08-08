package jsengine.parser

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

    private def literalObjectToFunctionCall(o: JSLiteralObject): Call = {
    	def toCode(property: (JSExpression, JSExpression)):Assign = {
    		property match {
    		  	case (name,value) => Assign(name,value)
    		}
    	}
    	val properties = o match { case JSLiteralObject(properties) => properties }
    	val source = properties.map((property) => property match { case (name,value) => Assign(name,value) })
    	val function = JSFunction(None,List(),source)
    	Call(function,List())
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
				  Call(BuiltIn(name),args)
			  }
			  case Lookup(expr,index) => Lookup(rewriteExpression(expr),rewriteExpression(index))
			  case New(function,args) => New(rewriteExpression(function),args.map(rewriteExpression _))
			  case Call(function,args) => Call(rewriteExpression(function),args.map(rewriteExpression _))
			  case Assign(left,value) => Assign(rewriteExpression(left), rewriteExpression(value))
			  case PostfixExpression(expression,Operator("--")) => Call(BuiltIn(")--"),List(expression))
			  case PostfixExpression(expression,Operator("++")) => Call(BuiltIn(")++"),List(expression))
			  case JSArrayLiteral(elements) => Call(BuiltIn("array"),elements.map((oe) => rewriteExpression(optionToUndefined(oe))))
			  case JSFunction(functionName,arguments, source)  => {
			     val functionSource = ((List[JSStatement]() /: source) ((x,y) => { x ::: (rewriteStatement(y))}))
			     JSFunction(functionName,arguments,functionSource)
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
			  case JSBlock(statements) => List(JSBlock((List[JSStatement]() /: statements) ((x,y) => { x ::: (rewriteStatement(y))})))
			  case VariableDeclarations(declarations) =>  ((List[JSStatement]() /: declarations) ((x,y) => { x ::: (rewriteStatement(y))}))
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
				        LabeledCaseClause(label,(List[JSStatement]() /: statements) ({((x,y) => { x ::: rewriteStatement(y) })}))
				      case DefaultClause(statements) => 
				        DefaultClause((List[JSStatement]() /: statements) ({((x,y) => { x ::: rewriteStatement(y) })}))
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
    	  case JSSource(statements) => JSSource((List[JSStatement]() /: statements) ((x,y) => { x ::: (rewriteStatement(y))}))
    	}
    }
    
    
/*
	private def templateRewrite[T <: ASTNode](node: T): T = {
			node match {
			  case JSExpression(expressions) => JSExpression(assignments.map(((x) => templateRewrite[JSBaseExpression](x))))
			  case AssignmentExpression(operator, leftHand, righthand) 
			  case ConditionalExpression(condition, trueExpression, falseExpression) 
			  case BinaryExpression(right,extensions) 
			  case BinaryExtension(operator,expr)
			  case UnaryExpression(operators,expression) 
			  case PostfixExpression(expression,operator) 
			  case CallExpression(newCount, function, applications)
			  case ApplyArguments(arglist) 
			  case ApplyLookup(expr: JSBaseExpression) 
			  case JSArrayLiteral(elements) 
			  case JSFunction(functionName,arguments, source) 
			  case JSBoolean(value) 
			  case JSIdentifier(value) 
			  case JSNativeCall(identifier) 
			  case Operator(value)
			  case JSLiteralObject(properties) 
			  case JSNumber(val value: String) 
			  case JSString(value) 
			  case JSRegexLiteral(value) 
			  case JSSource(sourceElements) 
			  case JSBlock(statements) 
			  case VariableStatement(variableDeclararations) 
			  case VariableDeclarations(declarations) 
			  case VariableDeclaration(name, initialValue) 
			  case EmptyStatement() 
			  case IfStatement(condition, whenTrue, whenFalse) 
			  case DoWhile (statement, condition) 
			  case While (condition, statement) 
			  case For(init, update,body) 
			  case ForInit(init)
			  case ForInUpdate(statement) 
			  case ForSemicolonUpdate(test, update) 
			  case ContinueStatement(label) 
			  case BreakStatement(label) 
			  case ReturnStatement(value) 
			  case WithStatement(expr, statement) 
			  case SwitchStatement(expr, cases) 
			  case LabeledCaseClause(label, statements) 
			  case DefaultClause(statements) 
			  case LabeledStatement(label, statement) 
			  case ThrowStatement(expr) 
			  case TryStatement(block, tryTail) 
			  case TryTail(id, catchBlock, finallyBlock)
			  case DebuggerStatement() 
			  case JSUndefined() 
			  case _ => throw new RuntimeException("Implementation error: missing handling of AST node: "+_)
			}
	}
*/  
}