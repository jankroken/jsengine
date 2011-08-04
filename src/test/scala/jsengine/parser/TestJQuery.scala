package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.getASTOrFail
import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject
import ParserTestSupport.verifyArrayLiteral
import ParserTestSupport.verifyExpression
import ParserTestSupport.verifyParsing
import ParserTestSupport.verifySource
import jsengine.parser.JSParser
import jsengine.ast._
import scala.io.Source

class TestJQuery {

    @Test def testSimpleNew {
    	val result = JSParser.parse(JSParser.source,Source.fromURL(this.getClass().getClassLoader().getResource("jquery-1.6.2.js")).reader)
    	println(result)
    }

}