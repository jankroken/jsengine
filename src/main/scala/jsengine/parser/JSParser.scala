package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast._
import jsengine.library.BuiltinObjects

object JSParser extends RegexParsers {
  
	def source: Parser[JSSource] = repsep(sourceElement,";") <~ "\\z".r ^^ { case sourceElements  => new JSSource(sourceElements) }
	
	
	/**
	 *  Expressions
	 */
	
	def expression : Parser[JSBaseExpression] = conditionalExpression ~ opt("," ~ expression) ^^ 
	    { case ex ~ None => ex
	      case ex ~ Some(x ~ JSExpression(exlist)) =>  JSExpression(ex :: exlist)
	      case ex ~ Some(x ~ expr) => JSExpression(List(ex,expr))
	    } 

	def primaryExpression: Parser[JSBaseExpression] = simpleLiteral | identifier | jsobject | nativeCall | "(" ~> expression <~ ")"

	def memberExpression : Parser[JSBaseExpression] = primaryExpression | functionExpression 
	
	def applyExtension : Parser[ApplicationExtension] = arrayLookupExtension | propertyLookupExtension | applyArguments
	
    def arrayLookupExtension : Parser[ApplyLookup] = "[" ~> expression <~ "]" ^^ { ApplyLookup(_) }
	def propertyLookupExtension: Parser[ApplyLookup] =  "." ~> identifier ^^ { ApplyLookup(_) }

	def applyArguments : Parser[ApplyArguments] = "(" ~> repsep(assignmentExpression,",") <~ ")" ^^ { ApplyArguments(_) }
	
	def callExpression : Parser[JSBaseExpression] = rep("new") ~ memberExpression ~ rep(applyExtension) ^^ {
	    case List() ~ expr ~ List() => expr
	    case news ~ expr ~ args => CallExpression(news.size,expr,args)
	}
	
	def assignmentOperator : Parser[Operator] = ( "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | ">>=" | ">>>=" | "&=" | "^=" | "|=" ) ^^ { Operator(_) }
	def assignmentExpression : Parser[JSBaseExpression] = callExpression ~ opt (assignmentOperator ~ conditionalExpression) ^^ {
	  case expr ~ None => expr
	  case left ~ Some(operator ~ right) => AssignmentExpression(operator,left,right)
	}

	def leftHandSideExpression = assignmentExpression
	
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

	def relationalOperator = ("<" | ">" | "<=" | ">=" | "instanceof" | "in")  ^^ { Operator(_) }
	def relationalExtension = relationalOperator ~ shiftExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def relationalExpression = shiftExpression ~ rep(relationalExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def equalityOperator = ("==" | "!=" | "===" | "!==")  ^^ { Operator(_) }
	def equalityExtension = equalityOperator ~ relationalExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def equalityExpression = relationalExpression ~ rep(equalityExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}

	def bitwiseAndOperator = "&" ^^ { Operator(_) }
	def bitwiseAndExtension = bitwiseAndOperator ~ equalityExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseAndExpression = equalityExpression ~ rep(bitwiseAndExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def bitwiseXorOperator = "^" ^^ { Operator(_) }
	def bitwiseXorExtension = bitwiseXorOperator ~ bitwiseAndExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseXorExpression = bitwiseAndExpression ~ rep(bitwiseXorExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def bitwiseOrOperator = "|" ^^ { Operator(_) }
	def bitwiseOrExtension = bitwiseOrOperator ~ bitwiseXorExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def bitwiseOrExpression = bitwiseXorExpression ~ rep(bitwiseOrExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def logicalAndOperator = "&&" ^^ { Operator(_) }
	def logicalAndExtension = logicalAndOperator ~ bitwiseOrExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def logicalAndExpression = bitwiseOrExpression ~ rep(logicalAndExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def logicalOrOperator = "||" ^^ { Operator(_) }
	def logicalOrExtension = logicalOrOperator ~ logicalAndExpression ^^ { case oper ~ expr => BinaryExtension(oper,expr) }
	def logicalOrExpression = logicalAndExpression ~ rep(logicalOrExtension) ^^ {
	  case expr ~ List() => expr
	  case expr ~ extensions => BinaryExpression(expr,extensions)
	}
	
	def conditionalExpression : Parser[JSBaseExpression]= logicalOrExpression ~ opt("?" ~> conditionalExpression ~ (":" ~> conditionalExpression)) ^^ {
	  case expr ~ None => expr
	  case expr ~ Some(trueExpr ~ falseExpr) => ConditionalExpression(expr,trueExpr,falseExpr)
	}
	
	def jsobject : Parser[JSLiteralObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^	 
		{ case keysAndValues:List[(PropertyName,JSBaseExpression)] => JSLiteralObject(List[(PropertyName,JSBaseExpression)]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(PropertyName, JSBaseExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[PropertyName] = identifier | stringLiteral | numericLiteral
	def propertyValue : Parser[JSBaseExpression] = assignmentExpression
	
	def identifier : Parser[JSString] = not("""(function|new|var)\b""".r) ~> """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { JSString(_)}

	def stringLiteral : Parser[JSString] = doubleQuotedStringLiteral | singleQuotedStringLiteral
	def doubleQuotedStringLiteral = """"([^"\\\n]|\\[\\\n'"bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*"""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString('\"'))} 
	def singleQuotedStringLiteral = """'([^'\\\n]|\\[\\\n'"bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*'""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString('\''))} 
	def regexLiteral : Parser[JSRegexLiteral] = """/([^/\\\n]|\\[\\\n'"/bfnrtv0]|\\x[0-9a-fA-F]{2}|\\u[0-9a-fA-F]{4})*/""".r ^^ { x => JSRegexLiteral(StringLiteral(x).getUnqotedString('/'))} 
	
	def arrayLiteral : Parser[JSArrayLiteral] = "[" ~> rep(opt(assignmentExpression) <~ ",") ~ opt(assignmentExpression) <~ "]" ^^ { 
	  	case exprlist ~ None  => JSArrayLiteral(exprlist)
	  	case exprlist ~ Some(expr) => JSArrayLiteral(exprlist ::: Some(expr) :: List())
	}
	def simpleLiteral : Parser[JSBaseExpression] = numericLiteral | stringLiteral | arrayLiteral | regexLiteral
	
	def numericLiteral : Parser[JSNumber] = smallDecimalNumber | largeDecimalNumber | hexadecimalNumber
	def largeDecimalNumber : Parser[JSNumber] = """[1-9][0-9]*(\.[0-9]*)?([eE][+-]?[0-9]*)?""".r  ^^ { JSNumber(_) }
	def smallDecimalNumber : Parser[JSNumber] = """0?\.[0-9]*([eE][+-][0-9]*)?""".r  ^^ { JSNumber(_) }
	def hexadecimalNumber : Parser[JSNumber] = """0[Xx][0-9a-fA-F]+""".r  ^^ { JSNumber(_) }
	

	def functionExpression : Parser[JSFunction]= "function" ~> opt(identifier) ~ ( "(" ~> repsep(identifier,",") <~ ")" ) ~ ( "{" ~> repsep(sourceElement,";") <~ "}" ) ^^
		{ case name ~ arguments  ~ sourceElements  => JSFunction(name, arguments, sourceElements) }

	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~ "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) }
	
	/**
	 * Statements
	 */

	def statement: Parser[JSStatement] = block | variableDeclaration | expressionStatement
	def block : Parser[JSBlock]= "{" ~> repsep(statement,";") <~ "}" ^^ { JSBlock(_) }
	
	def expressionStatement = not("""(function|,)\b""".r) ~> expression
	
	def sourceElement: Parser[JSSourceElement] = expression | statement;

	def variableDeclaration : Parser[VariableDeclarations] = "var" ~> repsep(singleVariable,",") ^^ { VariableDeclarations(_) }
	
	def singleVariable: Parser[VariableDeclaration] = identifier ~! opt("=" ~> assignmentExpression) ^^ {
	  case identifier ~ None => VariableDeclaration(identifier,None)
	  case identifier ~ Some(initialValue) => VariableDeclaration(identifier,Some(initialValue))
	}
	
	def testNot: Parser[JSString] = not("""if\b""".r) ~> identifier
}