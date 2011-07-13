package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast.JSObject
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSString
import jsengine.ast.PropertyName
import jsengine.ast.JSFunction
import jsengine.ast.JSNumber
import jsengine.ast.JSUndefined
import jsengine.ast.JSNativeCall
import jsengine.ast.JSExpression
import jsengine.ast.JSSourceElement
import jsengine.ast.Assignment
import jsengine.ast.VariableAssignment
import jsengine.ast.VariableDeclaration
import jsengine.ast.VariableDeclarations
import jsengine.ast.JSSource
import jsengine.library.BuiltinObjects

object JSParser extends RegexParsers {
  
	def source: Parser[JSSource] = repsep(sourceElement,";") <~ "\\z".r ^^ { case sourceElements  => new JSSource(sourceElements) }
	def expression : Parser[JSExpression] = jsobject | stringLiteral | numericLiteral | nativeCall | functionExpression | variableAssignment; 
	def jsobject : Parser[JSLiteralObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^ 
		{ keysAndValues:List[(PropertyName,JSExpression)] => JSLiteralObject(List[(PropertyName,JSExpression)]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(PropertyName, JSExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[PropertyName] = identifier | stringLiteral | numericLiteral
	
	def identifier : Parser[JSString] = """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { JSString(_)}
	def stringLiteral : Parser[JSString] = """"[^"]*"""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString)}
	def numericLiteral : Parser[JSNumber] = "[0-9][0-9]*".r ^^ { x:String => JSNumber(x) }
	
	def propertyValue : Parser[JSExpression] = expression

	def functionExpression : Parser[JSFunction]= "function" ~! opt(identifier) ~ "(" ~! repsep(identifier,",") ~ ")" ~ "{" ~! repsep(sourceElement,";") ~ "}" ^^
		{ case "function" ~ name ~ "(" ~ arguments  ~ ")" ~ "{" ~ sourceElements  ~ "}" => JSFunction(name, arguments, sourceElements) }
	
	def sourceElement: Parser[JSSourceElement] = expression | variableDeclaration;
	
	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~! "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) }
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
	
	def testNot: Parser[JSString] = not("""if\b""".r) ~ identifier ^^ { case _ ~ x => x }
}