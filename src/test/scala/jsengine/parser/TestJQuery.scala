package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport._
import jsengine.rewriter._
import jsengine.ast._
import scala.io.Source

class TestJQuery {

  
  
     def testFile(filename: String) {
    	val result = JSParser.parse(JSParser.source,Source.fromURL(this.getClass().getClassLoader().getResource(filename)).reader)
    	println(result)
    	result match {
    	  case JSParser.Success(ast,_) => println(JSCallRewriter.rewriteSource(ast))
    	  case JSParser.NoSuccess(message,src) => println("failed")	
    	}
    }

     @Test def testPart1 {
    	 testFile("jquery_part1.js")
     }
     
     @Test def testPart2 {
    	 testFile("jquery_part2.js")
     }

     @Test def testPart3 {
    	 testFile("jquery_part3.js")
     }

     @Test def testPart4 {
    	 testFile("jquery_part4.js")
     }
     
     @Test def testPart5 {
    	 testFile("jquery_part5.js")
     }
     
     @Test def testPart6 {
    	 testFile("jquery_part6.js")
     }

     @Test def testComplete {
    	 testFile("jquery-1.6.2.js")
     }
}