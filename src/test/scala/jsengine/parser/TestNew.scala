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
import jsengine.ast._

class TestNew {

    @Test def testSimpleNew {
    	val source = "new Foo()"
    	val ast = CallExpression(1,JSIdentifier("Foo"),List(ApplyArguments(List())))
    	verifyExpression(source,ast)
    	
    }

    @Test def testMultipleNew {
    	val source = "new new new Foo()(1)"
    	val ast = CallExpression(3,JSIdentifier("Foo"),List(ApplyArguments(List()),ApplyArguments(List(JSNumber("1")))))
    	verifyExpression(source,ast)
    }


}