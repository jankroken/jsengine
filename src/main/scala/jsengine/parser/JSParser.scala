package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast._
import jsengine.library.BuiltinObjects

object JSParser extends RegexParsers {
  
	def source: Parser[JSSource] = repsep(sourceElement,";") <~ "\\z".r ^^ { case sourceElements  => new JSSource(sourceElements) }
	
	
	/**
	 *  Expressions
	 */
	
	def expression(withIn: Boolean) : Parser[JSBaseExpression] = conditionalExpression(withIn) ~ opt("," ~ expression(withIn)) ^^ 
	    { case ex ~ None => ex
	      case ex ~ Some(x ~ JSExpression(exlist)) =>  JSExpression(ex :: exlist)
	      case ex ~ Some(x ~ expr) => JSExpression(List(ex,expr))
	    } 

	def primaryExpression: Parser[JSBaseExpression] = simpleLiteral | identifier | jsobject | nativeCall | functionExpression | "(" ~> expression(true) <~ ")"

	def memberExpression : Parser[JSBaseExpression] = primaryExpression | functionExpression 
	
	def applyExtension : Parser[ApplicationExtension] = arrayLookupExtension | propertyLookupExtension | applyArguments
	
    def arrayLookupExtension : Parser[ApplyLookup] = "[" ~> expression(true) <~ "]" ^^ { ApplyLookup(_) }
	def propertyLookupExtension: Parser[ApplyLookup] =  "." ~> identifier ^^ { 
	  case JSIdentifier(id) => ApplyLookup(JSString(id))
	}

	def applyArguments : Parser[ApplyArguments] = "(" ~> repsep(conditionalExpression(true),",") <~ ")" ^^ { ApplyArguments(_) }
	
	def callExpression : Parser[JSBaseExpression] = rep("new") ~ memberExpression ~ rep(applyExtension) ^^ {
	    case List() ~ expr ~ List() => expr
	    case news ~ expr ~ args => CallExpression(news.size,expr,args)
	}
	
	def assignmentOperator : Parser[Operator] = ( "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | ">>=" | ">>>=" | "&=" | "^=" | "|=" ) ^^ { Operator(_) }
	def assignmentExpression(withIn: Boolean) : Parser[JSBaseExpression] = callExpression ~ opt (assignmentOperator ~ conditionalExpression(withIn)) ^^ {
	  case expr ~ None => expr
	  case left ~ Some(operator ~ right) => AssignmentExpression(operator,left,right)
	}

	def leftHandSideExpression = assignmentExpression(true)
	
	def postfixOperator:Parser[Operator] = ( "++" | "--" ) ^^ { Operator(_) }

	// need to avoid newlines here, a \n ++ b will be parsed as (a++) b instead of a (++b)
	def postfixExpression = leftHandSideExpression ~ opt(postfixOperator) ^^ { 
	  case expr ~ None => expr
	  case expr ~ Some(operator) => PostfixExpression(expr,operator) 
	}
	
	def unaryOperator = ( "delete" | "void" | "typeof" | "++" | "--" | "+" | "-" | "~" | "!" ) ^^ { Operator(_) }
	
	def unaryExpression = rep(unaryOperator) ~ postfixExpression ^^ { 
	  case List() ~ expr => expr
	  case unaries ~ expr => UnaryExpression(unaries, expr)
	}
	
	def multiplicationOperator = ("*" | "/" | "%" ) ^^ { Operator(_) }
	def multiplicationExtension = multiplicationOperator ~ unaryExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def multiplicationExpression = unaryExpression ~ rep(multiplicationExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}

	def additiveOperator = ("+" | "-") ^^ { Operator(_) }
	def additiveExtension = additiveOperator ~ multiplicationExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def additiveExpression = multiplicationExpression ~ rep(additiveExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def shiftOperator = ("<<" | ">>" | ">>>") ^^ { Operator(_) }
	def shiftExtension = shiftOperator ~ additiveExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def shiftExpression = additiveExpression ~ rep(shiftExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}

	def relationalOperator(withIn: Boolean) = {
			if (withIn) { 
			  ( "<=" | ">=" | "instanceof" | "in" | "<" | ">" )  ^^ { Operator(_) } 
			} else {
			  ( "<=" | ">=" | "<" | ">" | "instanceof")  ^^ { Operator(_) }
			}
	}
	def relationalExtension(withIn: Boolean) = relationalOperator(withIn) ~ shiftExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def relationalExpression(withIn: Boolean) = shiftExpression ~ rep(relationalExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def equalityOperator = ("===" | "==" | "!==" |"!=")  ^^ { Operator(_) }
	def equalityExtension(withIn: Boolean) = equalityOperator ~ relationalExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def equalityExpression(withIn: Boolean) = relationalExpression(withIn) ~ rep(equalityExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}

	def bitwiseAndOperator = "&(?!&)".r ^^ { Operator(_) }
	def bitwiseAndExtension(withIn: Boolean) = bitwiseAndOperator ~ equalityExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseAndExpression(withIn: Boolean) = equalityExpression(withIn) ~ rep(bitwiseAndExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def bitwiseXorOperator = "^" ^^ { Operator(_) }
	def bitwiseXorExtension(withIn: Boolean) = bitwiseXorOperator ~ bitwiseAndExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseXorExpression(withIn: Boolean) = bitwiseAndExpression(withIn) ~ rep(bitwiseXorExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def bitwiseOrOperator = "\\|(?!\\|)".r ^^ { Operator(_) }
	def bitwiseOrExtension(withIn: Boolean) = bitwiseOrOperator ~ bitwiseXorExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseOrExpression(withIn: Boolean) = bitwiseXorExpression(withIn) ~ rep(bitwiseOrExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def logicalAndOperator = "&&" ^^ { Operator(_) }
	def logicalAndExtension(withIn: Boolean) = logicalAndOperator ~ bitwiseOrExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def logicalAndExpression(withIn: Boolean) = bitwiseOrExpression(withIn) ~ rep(logicalAndExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def logicalOrOperator = "||" ^^ { Operator(_) }
	def logicalOrExtension(withIn: Boolean) = logicalOrOperator ~ logicalAndExpression(withIn) ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def logicalOrExpression(withIn: Boolean) = logicalAndExpression(withIn) ~ rep(logicalOrExtension(withIn)) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def conditionalExpression(withIn: Boolean) : Parser[JSBaseExpression]= logicalOrExpression(withIn) ~ opt("?" ~> conditionalExpression(withIn) ~ (":" ~> conditionalExpression(withIn))) ^^ {
	  case expr ~ None => expr
	  case expr ~ Some(trueExpr ~ falseExpr) => ConditionalExpression(expr,trueExpr,falseExpr)
	}
	
	def jsobject : Parser[JSLiteralObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^	 
		{ case keysAndValues:List[(PropertyName,JSBaseExpression)] => JSLiteralObject(List[(PropertyName,JSBaseExpression)]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(PropertyName, JSBaseExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[PropertyName] = identifier | stringLiteral | numericLiteral
	def propertyValue : Parser[JSBaseExpression] = conditionalExpression(true)
	
	
	def keywords = """(function|new|var|while|for|do|break|continue|with|switch|case|break|default|try|catch|finally|debugger|if|else|throw|return|typeof|void|delete|instanceof)\b""".r
	def identifierString = """[a-zA-Z_$][a-zA-Z0-9_$]*""".r
	def identifier : Parser[JSIdentifier] = not(keywords) ~> identifierString ^^ { JSIdentifier(_)}

	def stringLiteral : Parser[JSString] = doubleQuotedStringLiteral | singleQuotedStringLiteral
	def doubleQuotedStringLiteral = """"([^"\\\n]|\\[\\\n'"bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*"""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString('\"'))} 
	def singleQuotedStringLiteral = """'([^'\\\n]|\\[\\\n'"bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*'""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString('\''))} 
//	def regexLiteral : Parser[JSRegexLiteral] = """/([^/\\\n]|\\[\\\n'"/bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*/""".r ^^ { JSRegexLiteral(_)}
	def regexLiteral : Parser[JSRegexLiteral] = """/(\\/|[^/])*/[a-z]*""".r ^^ { JSRegexLiteral(_) }
	
	def arrayLiteral : Parser[JSArrayLiteral] = "[" ~> rep(opt(assignmentExpression(true)) <~ ",") ~ opt(assignmentExpression(true)) <~ "]" ^^ { 
	  	case exprlist ~ None  => JSArrayLiteral(exprlist)
	  	case exprlist ~ Some(expr) => JSArrayLiteral(exprlist ::: Some(expr) :: List())
	}
	def simpleLiteral : Parser[JSBaseExpression] = numericLiteral | stringLiteral | arrayLiteral | regexLiteral
	
	def numericLiteral : Parser[JSNumber] = smallDecimalNumber | largeDecimalNumber | hexadecimalNumber | zero
	def zero = "0" ^^ { JSNumber(_) }
	def largeDecimalNumber = """[1-9][0-9]*(\.[0-9]*)?([eE][+-]?[0-9]*)?""".r  ^^ { JSNumber(_) }
	def smallDecimalNumber = """0?\.[0-9]*([eE][+-][0-9]*)?""".r  ^^ { JSNumber(_) }
	def hexadecimalNumber  = """0[Xx][0-9a-fA-F]+""".r  ^^ { JSNumber(_) }
	

	def functionExpression : Parser[JSFunction]= "function" ~> opt(identifier) ~! ( "(" ~> repsep(identifier,",") <~ ")" ) ~ ( "{" ~> repsep(sourceElement,";") <~ "}" ) ^^
		{ case name ~ arguments  ~ sourceElements  => JSFunction(name, arguments, sourceElements) }

	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~ "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) }
	
	/**
	 * Statements
	 */

	def statement: Parser[JSStatement] = ifStatement | block | variableDeclaration(true) | expressionStatement | iterationStatement | 
										 switchStatement  | break | continue | withStatement | tryStatement | throwStatement | debugger |
										 returnStatement
										 
										 
	def block : Parser[JSBlock]= "{" ~> repsep(statement,";") <~ "}" ^^ { JSBlock(_) }
	
	def expressionStatement = not("""(function|,)\b""".r) ~> expression(true)
	
	def ifStatement = "if" ~! "(" ~> expression(true) ~ ")" ~ body ~ opt("else" ~> body) ^^ {
	  case expr ~ ")" ~ truePart ~ falsePart => IfStatement(expr,truePart,falsePart)
	}
	
	def sourceElement: Parser[JSStatement] = functionExpression | statement;
//	def sourceElement: Parser[JSStatement] = statement;

	def variableDeclaration(withIn: Boolean) : Parser[VariableDeclarations] = "var" ~> repsep(singleVariable(withIn),",") ^^ { VariableDeclarations(_) }
	
	def singleVariable(withIn: Boolean): Parser[VariableDeclaration] = identifier ~ opt("=" ~> conditionalExpression(withIn)) ^^ {
	  case identifier ~ None => VariableDeclaration(identifier,None)
	  case identifier ~ Some(initialValue) => VariableDeclaration(identifier,Some(initialValue))
	}
	
	def iterationStatement = whileStatement | doWhileStatement | forStatement
	
	def whileStatement = "while" ~> ( "(" ~> expression(true) <~ ")" ) ~ body ^^ { case cond ~ body => While(cond,body) }
	def doWhileStatement = "do" ~> body ~ ( "while" ~ "(" ~> expression(true) <~ ")" ) ^^  { case body ~ cond => DoWhile(body,cond) }
	
	def body = blockStatement | whileSingleStatement
	def whileSingleStatement : Parser[JSStatement] = not("{") ~> statement
	def blockStatement = "{" ~> repsep(statement,";") <~ "}" ^^ { JSBlock(_) }
	
	def forStatement: Parser[ForStatement] = "for" ~ "(" ~> opt(forInit) ~ forUpdate ~ ")"  ~ statement ^^ {
	  case init ~ update ~ ")" ~ statement => ForStatement(init,update,statement)
	}
	
	def forUpdate = forInUpdate | forSemicolonUpdate
	
	def forSemicolonUpdate = ";" ~> opt(expression(true)) ~ ";" ~ opt(statement) ^^ { case test ~ ";" ~ update => ForSemicolonUpdate(test,update) }
	
	def forInUpdate = "in" ~> expression(true) ^^ { ForInUpdate(_) }
	  
	def forInit = ( variableDeclaration(false) | forInitExpression ) ^^ { ForInit(_) }
	  
	def forInitExpression : Parser[JSStatement] = not("""var\z""".r) ~> expression(false)
	
	def continue = "continue" ~> opt(identifier) ^^ { ContinueStatement(_) }
	def break = "break" ~> opt(identifier)  ^^ { BreakStatement(_) }
	def returnStatement = "return" ~> opt(expression(true)) ^^ { ReturnStatement(_) }
	
	def withStatement = "with" ~ "(" ~> expression(true) ~ ")" ~ statement ^^ { case expr ~ ")" ~ statement => WithStatement(expr,statement) }
	
	def switchStatement = "switch" ~> ( "(" ~> expression(true) <~ ")" ~ "{" ) ~ rep(caseClause) ~ opt(defaultClause) <~ "}" ^^ {
	  case expr ~ cases ~ None => SwitchStatement(expr,cases)
	  case expr ~ cases ~ Some(statement) => SwitchStatement(expr,cases :+ statement) 
	}
	
	def caseClause = "case" ~> expression(true) ~ ":" ~ repsep(statement,";") ^^ { case expr ~ ":" ~ statements => LabeledCaseClause(expr,statements) } 
	def defaultClause = "default" ~ ":" ~> repsep(statement,";") ^^ { case statements => DefaultClause(statements) } 
	
	def labeledStatement = "@label" ~> identifier ~ ":" ~ statement ^^ { case label ~ ":" ~ statement => LabeledStatement(label,statement) }
	
	def throwStatement = "throw" ~> expression(true) ^^ { ThrowStatement(_) }
	
	def tryStatement = "try" ~! block ~ tryTail ^^ { case "try" ~ block ~ tail => TryStatement(block,tail) } 
	def tryTail = catchTail | finallyTail
	def catchTail = "catch" ~ "(" ~> identifier ~ ")" ~ block ~ opt(finallyBlock) ^^ {
	  case identifier ~ ")" ~ block ~ finallyBlock => TryTail(Some(identifier),Some(block),finallyBlock)
	}
	def finallyTail = finallyBlock ^^ { case f => TryTail(None,None,Some(f)) }
	def finallyBlock = "finally" ~> block
	
	def debugger = "debugger" ^^ { case _ => DebuggerStatement() }
	
}