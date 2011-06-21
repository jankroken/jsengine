package jsengine.parser

import scala.util.parsing.combinator.RegexParsers
import jsengine.ast.EmptyPropertySet
import jsengine.ast.JSObject
import jsengine.ast.JSString
import jsengine.ast.JSFunction
import jsengine.ast.JSNumber
import jsengine.ast.JSNativeCall
import jsengine.ast.JSExpression
import jsengine.ast.JSSourceElement

object JSParser extends RegexParsers {
	def expression : Parser[JSExpression] = jsobject | stringLiteral | numericLiteral | nativeCall | functionExpression;
	def jsobject : Parser[JSObject] = "{" ~> repsep(propertyNameAndValue,",") <~ "}" ^^ 
		{ keysAndValues:List[(JSString,JSExpression)] => new JSObject(EmptyPropertySet,Map[JSString,JSExpression]() ++ keysAndValues) }
	def propertyNameAndValue : Parser[(JSString, JSExpression)] = propertyName ~ ":" ~ propertyValue ^^
		{ case propertyName ~ ":" ~ propertyValue => (propertyName, propertyValue) }
	def propertyName : Parser[JSString] = identifier | stringLiteral | numericLiteralString
	
	def identifier : Parser[JSString] = """[a-zA-Z][a-zA-Z0-9]*""".r ^^ { JSString(_)}
	def stringLiteral : Parser[JSString] = """"[^"]*"""".r ^^ { x => JSString(StringLiteral(x).getUnqotedString)}
	def numericLiteral : Parser[JSNumber] = "[0-9][0-9]*".r ^^ { x:String => JSNumber(x.toInt) }
	def numericLiteralString : Parser[JSString] = "[0-9][0-9]*".r ^^ { x:String => JSString(x) }
	
	def propertyValue : Parser[JSExpression] = expression

	def functionExpression : Parser[JSFunction]= "function" ~ opt(identifier) ~ "(" ~ repsep(identifier,",") ~ ")" ~ "{" ~ repsep(expression,";") ~ "}" ^^
		{ case "function" ~ name ~ "(" ~ arguments  ~ ")" ~ "{" ~ expressions  ~ "}" => JSFunction(name, arguments, expressions) }
	
	def sourceElement: Parser[JSSourceElement] = expression ;
	
	def nativeCall:Parser[JSNativeCall] = "@NATIVECALL" ~ "(" ~ identifier ~ ")" ^^ { case "@NATIVECALL" ~ "(" ~ identifier ~ ")" => JSNativeCall(identifier) } 
	
}