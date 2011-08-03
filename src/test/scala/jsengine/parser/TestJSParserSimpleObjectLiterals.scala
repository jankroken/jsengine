package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import ParserTestSupport.verifyLiteralObject
import jsengine.ast._

class TestJSParserSimpleObjectLiterals {

    @Test def testEmptyObjectNoWhitespace {
    	val source = "{}"
    	val ast = JSLiteralObject(List())
    	verifyLiteralObject(source,ast)
    }
    
    @Test def testEmptyObjectSpaces {
    	val source = "{ }"
    	val ast = JSLiteralObject(List())
    	verifyLiteralObject(source,ast)
    }

    @Test def testEmptyObjectSpacesAndNewline {
    	val source = "{ \n }"
    	val ast = JSLiteralObject(List())
    	verifyLiteralObject(source,ast)
    }
    
    @Test def testObjectWithStringValue {
    	val source = """{ "key" : "value" }"""
    	val ast = JSLiteralObject(List((JSString("key"),JSString("value"))))
    	verifyLiteralObject(source,ast)
    }

    @Test def testObjectWithIdentifierKey {
    	val source = """{ key : "value" }"""
    	val ast = JSLiteralObject(List((JSIdentifier("key"),JSString("value"))))
    	verifyLiteralObject(source,ast)
    }

    
    @Test def testObjectWithOnlyKey {
    	val result = JSParser.parse(JSParser.jsobject,"""{ "key" }""")

    	result match { 
    	  	case JSParser.Success(jsobject,_) => fail("This is an invalid object literal, and should not be parsed")
    	  	case JSParser.NoSuccess(message,_) => println("FAILURE message="+message)
    	}
    }

    @Test def testNestedObject {
    	val source = """
    		{
    			name : {
    				first : "Bruce",
    				last : "Springsteen"
    			},
    			album : "The Darkness on the Edge of Town",
    			year: 1978,
    			1337 : "true"
    		 }
    	"""
    	val ast = JSLiteralObject(List(
    			(JSIdentifier("name"),JSLiteralObject(List(
    								   (JSIdentifier("first"),JSString("Bruce")),
    								   (JSIdentifier("last"),JSString("Springsteen"))
    							   ))),
    			(JSIdentifier("album"),JSString("The Darkness on the Edge of Town")),
    			(JSIdentifier("year"),JSNumber("1978")),
    			(JSNumber("1337"),JSString("true"))
    		))
    	verifyLiteralObject(source,ast)
    }

}