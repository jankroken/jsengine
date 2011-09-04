package jsengine

import scala.io.Source
import jsengine.rewriter.Rewriter
import jsengine.parser.JSParser
import jsengine.runtime.tree.RTSource
import jsengine.runtime.tree.RTEnvironmentRecord
import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class JSRunner {
  
    val context = new ExecutionContext()
    val env = new RTEnvironmentRecord()
  
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
		runRTSource(rtAST)
	}
	
	private def runRTSource(source: RTSource) = {
    	val result = source.evaluate(env)
    	println("result from source = "+result)
    	result match {
    	  case Stdlib_Undefined => ScalaReturnUndefined
    	  case bool: Stdlib_Boolean => ScalaReturnBoolean(bool.nativeBooleanValue)
    	  case number: Stdlib_Number => ScalaReturnNumber(number.nativeDoubleValue)
    	  case string: Stdlib_String => ScalaReturnString(string.nativeStringValue)
    	  case _ => ScalaReturnNotImplemented()
    	}
	}
	
}