package jsengine

import scala.io.Source
import jsengine.rewriter.Rewriter
import jsengine.parser.JSParser
import jsengine.runtime.tree._
import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class JSRunner {
  
    val context = new ExecutionContext()
    val env = new RTEnvironmentRecord(None)
  
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
	
	private def runRTSource(source: RTSource):ScalaReturn = {
	    try {
	    	val result = source.evaluate(env)
	    	println("result from source = "+result)
        val scalaReturnValue = result match {
          case obj:RTObject => ScalaReturn(obj)
          case _ => throw new RuntimeException("Unhandled return: "+result)
        }
        println("Scala Return: "+scalaReturnValue)
        scalaReturnValue
	    } catch {
	        case referenceError: RTReferenceError => 
	            println("ReferenceError: "+referenceError.msg)
              referenceError.printStackTrace()
	            ScalaReturnException(referenceError)
	        case typeError: RTTypeError => 
	            println("TypeError: "+typeError.msg)
              typeError.printStackTrace()
	            ScalaReturnException(typeError)
	    }
	}
	
}