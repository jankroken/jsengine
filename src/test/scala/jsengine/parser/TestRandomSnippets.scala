package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.getASTOrFail
import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject
import ParserTestSupport.verifySource
import jsengine.ast._

class TestRandomSnippets {

    @Test def testWhile {
    	val source = """
			function PrintDate() {
    			today = new Date() ;
    			document.write('Date: ', today.getMonth()+1, '/', today.getDate(), '/', today.getYear())
    		}    	
    	"""
    	val ast = While(JSIdentifier("true"),JSBlock(List()))
    	verifyFunction(source,ast)
    }


}