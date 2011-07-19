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
import jsengine.ast.JSFunction
import jsengine.ast.JSNativeCall
import jsengine.ast.ASTNode
import jsengine.ast.JSString
import jsengine.ast.JSNumber
import jsengine.ast.JSArrayLiteral
import jsengine.ast.NewExpression
import jsengine.ast.JSLiteralObject

class TestNew {

    @Test def testSimpleNew {
    	val source = "new Foo()"
    	val ast = NewExpression(1,JSString("Foo"),List(List()))
    	verifyParsing(JSParser.newExpression, source,ast)
    	
    }

    @Test def testOneElementArray {
    	val source = "[1]"
    	val ast = JSArrayLiteral(List(Some(JSNumber("1"))))
    	verifyArrayLiteral(source,ast)
    }

    @Test def testTwoElementArray {
    	val source = "[1,2]"
    	val ast = JSArrayLiteral(List(Some(JSNumber("1")),Some(JSNumber("2"))))
    	verifyArrayLiteral(source,ast)
    }

    @Test def testOneEmptyAndTwoElementArray {
    	val source = "[,1,2]"
    	val ast = JSArrayLiteral(List(None,Some(JSNumber("1")),Some(JSNumber("2"))))
    	verifyArrayLiteral(source,ast)
    }

    @Test def testEmptyTwoEmpty {
    	val source = "[,1,2,,]"
    	val ast = JSArrayLiteral(List(None,Some(JSNumber("1")),Some(JSNumber("2")),None))
    	verifyArrayLiteral(source,ast)
    }

}