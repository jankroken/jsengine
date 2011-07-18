package jsengine.parser

import org.junit.Assert.fail
import jsengine.ast.ASTNode
import jsengine.ast.JSFunction
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSSource
import jsengine.ast.JSNumber
import jsengine.ast.JSArrayLiteral
import org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers.is;

object ParserTestSupport {
  
	def getASTOrFail(parseResult:JSParser.ParseResult[ASTNode]):ASTNode = {
		parseResult match {
		  case JSParser.Success(ast,_) => return ast
		  case JSParser.NoSuccess(message,_) => {
			  fail(message)
			  return null // only to trick compiler
		  }
		}
	}

    def verifyParsing[T <: ASTNode](parser: JSParser.Parser[T], input: String, expectedAST: ASTNode) {
    	val result = JSParser.parse(parser,input)
    	val ast = getASTOrFail(result)

    	try {
    		assertThat(ast,is[Any](expectedAST))
    	} catch {
    	  case ae: AssertionError =>
	    	println("Parsed AST did not match expected AST")
	    	println("parsed AST   : "+ast)
	    	println("expected AST : "+expectedAST)
	    	throw ae
    	  case other =>
    		throw other
    	}
	}
    
	def verifyFunction(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSFunction](JSParser.functionExpression,source,expected)
	}

	def verifyLiteralObject(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSLiteralObject](JSParser.jsobject,source,expected)
	}

	def verifySource(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSSource](JSParser.source,source,expected)
	}

	def verifyNumericLiteral(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSNumber](JSParser.numericLiteral,source,expected)
	}

	def verifyArrayLiteral(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSArrayLiteral](JSParser.arrayLiteral,source,expected)
	}
}