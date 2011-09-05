package jsengine.rewriter

import jsengine.ast._

sealed case class SplitSource(declarations: List[Declare], fassigns: List[Assign], statements: List[JSStatement])
object SplitSource {
	def apply():SplitSource = SplitSource(List(),List(),List())
	def apply(statement: JSStatement):SplitSource = SplitSource(List(),List(),List(statement))
} 
sealed case class SplitCase(declarations: List[Declare], fassigns: List[Assign], cases: List[CaseClause])


object FunctionDeclarationRewriter {

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
			  case JSExpression(expressions) => JSExpression(expressions.map(rewriteExpression _))
			  case ConditionalExpression(condition, trueExpression, falseExpression) => {
				  ConditionalExpression(rewriteExpression(condition),
				  						rewriteExpression(trueExpression),
				  						rewriteExpression(falseExpression))
			  }
			  case Lookup(expr,index) => Lookup(rewriteExpression(expr),rewriteExpression(index))
			  case New(function,args) => New(rewriteExpression(function),args.map(rewriteExpression _))
			  case Call(function,args) => Call(rewriteExpression(function),args.map(rewriteExpression _))
			  case Assign(left,value) => Assign(rewriteExpression(left), rewriteExpression(value))
			  case PostfixExpression(expression,Operator("--")) => Call(BuiltIn(")--"),List(expression))
			  case PostfixExpression(expression,Operator("++")) => Call(BuiltIn(")++"),List(expression))
			  case JSArrayLiteral(elements) => Call(BuiltIn("array"),elements.map((oe) => rewriteExpression(optionToUndefined(oe))))
			  case function:JSFunction  => rewriteFunction(function)
			  case JSBoolean(value) => JSBoolean(value)
			  case JSIdentifier(value) => JSIdentifier(value)
			  case JSNativeCall(identifier) => JSNativeCall(identifier)
			  case JSNumber(value) => JSNumber(value) 
			  case JSString(value) => JSString(value) 
			  case JSRegexLiteral(value) => JSRegexLiteral(value)
			}
    }
  
    private def rewriteIf(ifStatement:IfStatement):SplitSource = {
    	val IfStatement(cond,whenTrue,optWhenFalse) = ifStatement
    	val newCond = rewriteExpression(cond)
    	val SplitSource(trueDeclares, trueFunctions, trueSource) = rewriteStatement(whenTrue)
    	optWhenFalse match {
    	  	case None => return SplitSource(trueDeclares, trueFunctions, List(IfStatement(newCond,JSBlock(trueSource),None)))
    	  	case Some(whenFalse) => {
    	  		val SplitSource(falseDeclares, falseFunctions, falseSource) = rewriteStatement(whenFalse)
    	  		return SplitSource(trueDeclares ::: falseDeclares, 
    	  					trueFunctions ::: falseFunctions, 
    	  					List(IfStatement(newCond,JSBlock(trueSource),Some(JSBlock(falseSource)))))
    	  	}
    	}
    	
    }
    
    private def rewriteCases(cases: List[CaseClause]):SplitCase = {
    	cases match {
    	  	case List() => SplitCase(List(),List(),List())
    	  	case LabeledCaseClause(label,source) :: tail => {
    	  		val SplitCase(tailDeclarations,tailFunctions,tailCases) = rewriteCases(tail)
    	  		val SplitSource(declarations,functions,statements) = rewriteStatementList(source)
    	  		SplitCase(declarations:::tailDeclarations,
    	  				  functions:::tailFunctions,
    	  				  LabeledCaseClause(label,statements) :: tailCases)
    	  	}
    	  	case List(DefaultClause(source)) => {
    	  		val SplitSource(declarations,functions,rewrittenStatements) = rewriteStatementList(source)
    	  		SplitCase(declarations,functions,List(DefaultClause(rewrittenStatements)))
    	  	}
    	}
    }
    
    private def rewriteSwitch(switchStatement: SwitchStatement):SplitSource = {
		val SwitchStatement(expr,cases) = switchStatement
		val sExpr = rewriteExpression(expr)
		val SplitCase(caseDeclarations,caseFunctions,rewrittenCases) = rewriteCases(cases)
		SplitSource(caseDeclarations,caseFunctions,List(SwitchStatement(sExpr,rewrittenCases)))
	} 
    
    private def rewriteTry(tryStatement: Try):SplitSource = {
    	val Try(statement,optCatchBlock,optFinallyBlock) = tryStatement
    	val SplitSource(tryDeclarations, tryFunctions, tryStatements) = rewriteStatement(statement)
    	val (optCatch,SplitSource(catchDecl,catchFunc,catchSource)) = optCatchBlock match {
    	  	case None => (None,SplitSource())
    	  	case Some(Catch(id,catchStatement)) => {
    	  		val SplitSource(catchDeclarations, catchFunctions, catchStatements) = rewriteStatement(catchStatement);
    	  		(Some(Catch(id,catchStatement)),SplitSource(catchDeclarations,catchFunctions,List()))
    	  	}
    	}
    	val (optFinally,SplitSource(finallyDecl,finallyFunc,finallySrc)) = optFinallyBlock match {
    	  case None => (None,SplitSource())
    	  case Some(source) => {
    		  val SplitSource(finallyDeclares, finallyFunctions, finallyStatements) = rewriteStatement(source)
    		  (Some(JSBlock(finallyStatements)),SplitSource(finallyDeclares,finallyFunctions,List()))
    	  }
    	}
    	val declares = tryDeclarations ::: catchDecl ::: finallyDecl
    	val functions = tryFunctions ::: catchFunc ::: finallyFunc
    	val resTry = Try(JSBlock(tryStatements),optCatch,optFinally)
    	return SplitSource(declares,functions,List(resTry))
    }
    
    private def rewriteFunction(func: JSFunction):JSFunctionExpression = {
    	val JSFunction(name,args,source) = func
    	val SplitSource(bodyDeclarations,bodyFunctions,bodySource) = rewriteStatementList(source);
    	val declaredVariables = bodyDeclarations.map((declare) => { val Declare(id) = declare; id })
    	JSFunctionExpression(name,args,declaredVariables,bodyFunctions:::bodySource)	
    }
    
    private def rewriteFunctionStatement(func: JSFunction) = {
    	val JSFunction(Some(name),_,_) = func 
    	val declare = Declare(name)
    	val assign = Assign(name,rewriteFunction(func))
    	SplitSource(List(declare),List(),List(assign))
    }
    
    private def rewriteStatement (statement: JSStatement) : SplitSource = {
    	statement match {
    	    case Declare(identifier) => SplitSource(List(Declare(identifier)), List(), List())
    	    case JSBlock(body) => { 
    	    	val SplitSource(blockDeclares, blockFunctions, blockStatements) = rewriteStatementList(body)
    	    	SplitSource(blockDeclares, blockFunctions, List(JSBlock(blockStatements)))
    	    }
			case EmptyStatement() => SplitSource(List(), List(), List(EmptyStatement()))
			case ifStatement@IfStatement(condition, whenTrue, whenFalse) => rewriteIf(ifStatement) 
			case DoWhile (statement, condition)  => { 
				val SplitSource(dwDeclare, dwFunctions, dwStatements) = rewriteStatement(statement)
				SplitSource(dwDeclare,dwFunctions,List(DoWhile(JSBlock(dwStatements),rewriteExpression(condition))))
			}
			case While (condition, statement) => { 
				val SplitSource(wDeclare, wFunctions, wStatements) = rewriteStatement(statement)
				SplitSource(wDeclare,wFunctions,List(While(rewriteExpression(condition),JSBlock(wStatements))))
			}
			case ContinueStatement(label) => SplitSource(statement)
			case ForIn(init,expr,body) => { 
				val SplitSource(initDeclares, initFunctions, initStatements) = rewriteStatement(init)
				val SplitSource(bodyDeclares, bodyFunctions, bodyStatements) = rewriteStatement(body)
				SplitSource(initDeclares ::: bodyDeclares, initFunctions ::: bodyFunctions, 
						List(ForIn(JSBlock(initStatements),
								   rewriteExpression(expr),
								   JSBlock(bodyStatements))))
			}
			case BreakStatement(label) => SplitSource(statement)
			case ReturnStatement(None) => SplitSource(statement)
			case ReturnStatement(Some(value))  => SplitSource(ReturnStatement(Some(rewriteExpression(value))))
			case WithStatement(expr, statement) => { 
				val SplitSource(withDeclares,withFunctions,withStatements) = rewriteStatement(statement)
				val withExpr = rewriteExpression(expr)
				SplitSource(withDeclares,withFunctions,List(WithStatement(withExpr,JSBlock(withStatements))))
			}
		    case switch @ SwitchStatement(expr, cases)  =>  rewriteSwitch(switch)
			case LabeledStatement(label, statement) => {
				val SplitSource(decl,func,source) = rewriteStatement(statement)
				SplitSource(decl,func,List(LabeledStatement(label,JSBlock(source))))
			}
			case tryStatement @ Try(statement,catchBlock,finallyBlock) => rewriteTry(tryStatement)
			case ThrowStatement(expr) => SplitSource(ThrowStatement(rewriteExpression(expr)))
			case DebuggerStatement() => SplitSource(statement)
			case func:JSFunction => rewriteFunctionStatement(func) 
			case expr:JSBaseExpression => SplitSource(rewriteExpression(expr))
			case unhandledStatement => throw new RuntimeException("Implementation error: missing handling of AST node: "+unhandledStatement)
    	}
    }
    
	
	def rewriteStatementList(statements: List[JSStatement]):SplitSource = {
    	statements match {
    	  case List() => return SplitSource()
    	  case statement :: tail => {
    		  val SplitSource(tailDeclares, tailFunctions, tailStatements) = rewriteStatementList(tail)
    		  val SplitSource(declares,functions,statements) = rewriteStatement(statement)
    		  return SplitSource(declares ::: tailDeclares, functions ::: tailFunctions, statements ::: tailStatements)
    	  }
    	}
	}
  
    def rewriteSource (source: JSSource) : JSSource = {
    	source match {
    	  case JSSource(statements) => 
    	  		val SplitSource(declares, functions, rstatements) = rewriteStatementList(statements)
    	  		JSSource(declares ::: functions ::: rstatements)  
    	}
    }
        
}