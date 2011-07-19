package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast.JSObject
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSString
import jsengine.ast.JSRegexLiteral
import jsengine.ast.PropertyName
import jsengine.ast.JSFunction
import jsengine.ast.JSNumber
import jsengine.ast.JSUndefined
import jsengine.ast.JSNativeCall
import jsengine.ast.JSBaseExpression
import jsengine.ast.JSExpression
import jsengine.ast.NewExpression
import jsengine.ast.JSSourceElement
import jsengine.ast.JSArrayLiteral
import jsengine.ast.LookupExpression
import jsengine.ast.Operator
import jsengine.ast.Assignment
import jsengine.ast.VariableAssignment
import jsengine.ast.VariableDeclaration
import jsengine.ast.VariableDeclarations
import jsengine.ast.JSSource
import jsengine.library.BuiltinObjects

object JSParser extends RegexParsers {
  
	def source: Parser[JSSource] = repsep(sourceElement,";") <~ "\\z".r ^^ { case sourceElements  => new JSSource(sourceElements) }
	def expression : Parser[JSBaseExpression] = assignmentExpression ~ opt("," ~ expression) ^^ 
	    { case ex ~ None => ex
	      case ex ~ Some(x ~ JSExpression(exlist)) =>  JSExpression(ex :: exlist)
	      case ex ~ Some(x ~ expr) => JSExpression(List(ex,expr))
	    } 
	def assignmentOperator : Parser[Operator] = ( "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | ">>=" | ">>>=" | "&=" | "^=" | "|=" ) ^^ { Operator(_) }
	def assignmentExpression = newExpression
	
	def primaryExpression: Parser[JSBaseExpression] = simpleLiteral | identifier | jsobject | nativeCall | variableAssignment | "(" ~> expression <~ ")"

	def memberExpression : Parser[JSBaseExpression] = primaryExpression | functionExpression // | lookupExpression 
	
	private def lookupExpression: Parser[JSBaseExpression] = memberExpression ~ rep(lookupExtension) ^^ {
	    case expr ~ List() => expr
	    case expr ~ ( index :: tail ) => LookupExpression(expr, index :: tail)
	}
	private def lookupExtension : Parser[JSBaseExpression] = arrayLookupExtension | propertyLookupExtension	  
    private def arrayLookupExtension : Parser[JSBaseExpression] =  "[" ~> expression <~ "]"
	private def propertyLookupExtension: Parser[JSBaseExpression] =  "." ~> identifier
	
	def arguments : Parser[List[JSBaseExpression]] = "(" ~> repsep(assignmentExpression,",") <~ ")"
	def newExpression : Parser[JSBaseExpression] = realNewExpression | lookupExpression 
	private def realNewExpression : Parser[NewExpression] = news ~ memberExpression ~ argumentLists ^^ { 
	    case news1 ~ expr ~ args => NewExpression(news1.size,expr,args)
	}

	def news = "new" ~ rep("new") ^^ { case new1 ~ newlist => new1 :: newlist }
	def argumentLists = arguments ~ rep(arguments) ^^ { case args ~ argsList => args :: argsList }
	
	def jsobject : Parser[JSLiteralObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^ 
		{ case keysAndValues:List[(PropertyName,JSBaseExpression)] => JSLiteralObject(List[(PropertyName,JSBaseExpression)]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(PropertyName, JSBaseExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[PropertyName] = identifier | stringLiteral | numericLiteral
	def propertyValue : Parser[JSBaseExpression] = assignmentExpression
	
	def identifier : Parser[JSString] = not("""(function|new)\b""".r) ~> """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { JSString(_)}
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
	

	def functionExpression : Parser[JSFunction]= "function" ~ opt(identifier) ~ "(" ~ repsep(identifier,",") ~ ")" ~ "{" ~ repsep(sourceElement,";") ~ "}" ^^
		{ case "function" ~ name ~ "(" ~ arguments  ~ ")" ~ "{" ~ sourceElements  ~ "}" => JSFunction(name, arguments, sourceElements) }
	
	def sourceElement: Parser[JSSourceElement] = expression | variableDeclaration;
	
	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~ "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) }
	def variableDeclaration : Parser[VariableDeclarations] = "var" ~> repsep(singleVariable,",") ^^ {
	  case declarations => VariableDeclarations(declarations)
	}
	def singleVariable: Parser[VariableDeclaration] = identifier ~ opt("=" ~> expression) ^^ {
	  case identifier ~ None => VariableDeclaration(identifier,None)
	  case identifier ~ Some(initialValue) => VariableDeclaration(identifier,Some(initialValue))
	}
	def variableAssignment: Parser[VariableAssignment] = identifier ~ "=" ~ expression ^^ {
	  case identifier ~ "=" ~ expression => VariableAssignment(identifier,expression)
	}
	
	def testNot: Parser[JSString] = not("""if\b""".r) ~> identifier
}