package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import ParserTestSupport.verifyLiteralObject
import jsengine.ast.JSString
import jsengine.ast.JSNumber
import jsengine.ast.JSLiteralObject

class TestJSParserSimpleObjectLiterals {

    @Test def testEmptyObjectNoWhitespace {
    	val source = "{}"
    	val ast = JSLiteralObject(Map())
    	verifyLiteralObject(source,ast)
    }
    
    @Test def testEmptyObjectSpaces {
    	val source = "{ }"
    	val ast = JSLiteralObject(Map())
    	verifyLiteralObject(source,ast)
    }

    @Test def testEmptyObjectSpacesAndNewline {
    	val source = "{ \n }"
    	val ast = JSLiteralObject(Map())
    	verifyLiteralObject(source,ast)
    }
    
    @Test def testObjectWithStringValue {
    	val source = """{ "key" : "value" }"""
    	val ast = JSLiteralObject(Map(JSString("key") -> JSString("value")))
    	verifyLiteralObject(source,ast)
    }

    @Test def testObjectWithIdentifierKey {
    	val source = """{ key : "value" }"""
    	val ast = JSLiteralObject(Map(JSString("key") -> JSString("value")))
    	verifyLiteralObject(source,ast)
    }

    
    @Test def testObjectWithOnlyKey {
    	val result = JSParser.parse(JSParser.jsobject,"""{ "key" }""")

    	result match { 
    	  	case JSParser.Success(jsobject,_) => fail("This is an invalid object literal, and should not be parsed")
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
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
    	val ast = JSLiteralObject(Map(
    			JSString("name") -> JSLiteralObject(Map(
    								   JSString("first") -> JSString("Bruce"),
    								   JSString("last") -> JSString("Springsteen")
    							   )),
    			JSString("album") -> JSString("The Darkness on the Edge of Town"),
    			JSString("year") -> JSNumber("1978"),
    			JSString("1337") -> JSString("true")
    		))
    	verifyLiteralObject(source,ast)
    }

}