package jsengine.parser

import org.junit.Test

import ParserTestSupport.verifyExpression
import jsengine.ast._

class TestInvocation {

    @Test def testSimpleNew() {
    	val source = "new Foo()"
    	val ast = CallExpression(1,JSIdentifier("Foo"),List(ApplyArguments(List())))
    	verifyExpression(source,ast)
    	
    }

    @Test def testMultipleNew() {
    	val source = "new new new Foo()(1)"
    	val ast = CallExpression(3,JSIdentifier("Foo"),List(ApplyArguments(List()),ApplyArguments(List(JSNumber("1")))))
    	verifyExpression(source,ast)
    }

    @Test def testInvokeNewMember() {
    	val source = "new hello.foo()"
    	val ast = CallExpression(1,JSIdentifier("hello"),List(ApplyLookup(JSString("foo")), ApplyArguments(List())))
    	verifyExpression(source,ast)
    }

    @Test def testInvokeMember() {
    	val source = "hello.foo()"
    	val ast = CallExpression(0,JSIdentifier("hello"),List(ApplyLookup(JSString("foo")), ApplyArguments(List())))
 		verifyExpression(source,ast)
    }
}