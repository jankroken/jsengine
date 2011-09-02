package jsengine

import scala.io.Source
import jsengine.rewriter.Rewriter
import jsengine.parser.JSParser

class JSRunner {
	def run(source: Source):ScalaReturn = {
		ScalaReturnUndefined
	}

	def run(source: String):ScalaReturn = {
		val parseResult = JSParser.parse(JSParser.source,source)
		val ast = parseResult match {
    	  	case JSParser.Success(ast,_) => ast
    	    case JSParser.NoSuccess(message,src) => throw new RuntimeException("parsing failed: "+message)
		}
		val rtAST = Rewriter.rewrite(ast)
		println("AST: "+rtAST)
		ScalaReturnUndefined
	}
	
}