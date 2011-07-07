package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast.EmptyPropertySet
import jsengine.ast.JSObject
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSString
import jsengine.ast.JSFunction
import jsengine.ast.JSNumber
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
	def program: Parser[JSSource] = repsep(sourceElement,";") <~ "\\z".r ^^ { case sourceElements  => new JSSource(sourceElements) }
	def expression : Parser[JSExpression] = jsobject | stringLiteral | numericLiteral | nativeCall | functionExpression | variableAssignment; 
	def jsobject : Parser[JSLiteralObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^ 
		{ keysAndValues:List[(JSString,JSExpression)] => JSLiteralObject(Map[JSString,JSExpression]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(JSString, JSExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[JSString] = identifier | stringLiteral | numericLiteralString
	
	def identifier : Parser[JSString] = """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { JSString(_)}
	def stringLiteral : Parser[JSString] = """"[^"]*"""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString)}
	def numericLiteral : Parser[JSNumber] = "[0-9][0-9]*".r ^^ { x:String => JSNumber(x) }
	def numericLiteralString : Parser[JSString] = "[0-9][0-9]*".r ^^ { x:String => JSString(x) }
	
	def propertyValue : Parser[JSExpression] = expression

	def functionExpression : Parser[JSFunction]= "function" ~! opt(identifier) ~ "(" ~! repsep(identifier,",") ~ ")" ~ "{" ~! repsep(sourceElement,";") ~ "}" ^^
		{ case "function" ~ name ~ "(" ~ arguments  ~ ")" ~ "{" ~ sourceElements  ~ "}" => JSFunction(name, arguments, sourceElements) }
	
	def sourceElement: Parser[JSSourceElement] = expression | variableDeclaration;
	
	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~! "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) }
	def variableDeclaration : Parser[VariableDeclarations] = "var" ~> repsep(singleVariable,",") ^^ {
	  case declarations => VariableDeclarations(declarations)
	}
	def singleVariable: Parser[VariableDeclaration] = propertyName ~ opt("=" ~> expression) ^^ {
	  case propertyName ~ None => VariableDeclaration(propertyName,BuiltinObjects._undefined)
	  case propertyName ~ Some(initialValue) => VariableDeclaration(propertyName,initialValue)
	}
	def variableAssignment: Parser[VariableAssignment] = propertyName ~ "=" ~ expression ^^ {
	  case propertyName ~ "=" ~ expression => VariableAssignment(propertyName,expression)
	}
}