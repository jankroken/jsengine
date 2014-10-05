package jsengine.rewriter

import org.junit.Test

import jsengine.parser.JSParser
import scala.io.Source

class TestRewriter {

     def testFile(filename: String) {
    	val result = JSParser.parse(JSParser.source,Source.fromURL(this.getClass.getClassLoader.getResource(filename)).reader())
//    	println(result)
    	result match {
    	  case JSParser.Success(ast,_) => println(
    	      JSShrinkRewriter.rewriteSource(
    	          FunctionDeclarationRewriter.rewriteSource(
    	    	     JSShrinkRewriter.rewriteSource(
    	    	         JSCallRewriter.rewriteSource(
    	    	             JSInitialRewriter.rewriteSource(ast))))))
    	  case JSParser.NoSuccess(message,src) => println("failed")
    	}
    }
     
     @Test def testPart1() {
//       testFile("function1.js")
       testFile("jquery_part1.js")
     }
}
