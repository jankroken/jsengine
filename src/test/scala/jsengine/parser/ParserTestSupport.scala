package jsengine.parser

import org.junit.Assert.fail
import jsengine.ast.ASTNode
import org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers.is;

object ParserTestSupport {
  
	def getASTOrFail(parseResult:JSParser.ParseResult[ASTNode]):ASTNode = {
		parseResult match {
		  case JSParser.Success(ast,_) => return ast
		  case JSParser.Failure(message,_) => {
			  fail(message)
			  return null // only to trick compiler
		  }
		}
	}

    def verifyParsing[T <: ASTNode](parser: JSParser.Parser[T], input: String, expectedAST: ASTNode) {
    	val result = JSParser.parse(parser,input)
    	val ast = getASTOrFail(result)
    	assertThat(ast,is[Any](expectedAST))
    }


}