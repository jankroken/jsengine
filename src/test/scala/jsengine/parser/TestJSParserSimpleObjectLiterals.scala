package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString

class TestJSParserSimpleObjectLiterals {

    @Test def testEmptyObjectNoWhitespace {
    	val result = JSParser.parse(JSParser.jsobject,"{}")
    	println("result="+result)
    }
    
    @Test def testEmptyObjectSpaces {
    	val result = JSParser.parse(JSParser.jsobject,"{  }")
    	println("result="+result)
    }

    @Test def testEmptyObjectSpacesAndNewline {
    	val result = JSParser.parse(JSParser.jsobject,"{ \n }")
    	println("result="+result)
    }
    
    @Test def testObjectWithStringValue {
    	val result = JSParser.parse(JSParser.jsobject,"""{ "key" : "value" }""")

    	result match { 
    	  	case JSParser.Success(jsobject,_) => {
    	  			println("SUCCESS jsobject="+jsobject)
    	  			val value = jsobject.getProperty(JSString("key"))
    	  			assertThat(value,is[Any](Some(JSString("value"))))
    	  		}
    	  	case JSParser.Failure(message,_) => fail("this is a valid object literal, and should not fail: "+message)
    	}
    	
    }

    @Test def testObjectWithIdentifierKey {
    	val result = JSParser.parse(JSParser.jsobject,"""{ key : "value" }""")

    	result match { 
    	  	case JSParser.Success(jsobject,_) => {
    	  			println("SUCCESS jsobject="+jsobject)
    	  			val value = jsobject.getProperty(JSString("key"))
    	  			assertThat(value,is[Any](Some(JSString("value"))))
    	  		}
    	  	case JSParser.Failure(message,_) => fail("this is a valid object literal, and should not fail: "+message)
    	}
    	
    }

    
    @Test def testObjectWithOnlyKey {
    	val result = JSParser.parse(JSParser.jsobject,"""{ "key" }""")

    	result match { 
    	  	case JSParser.Success(jsobject,_) => fail("THis is an invalid object literal, and should not be parsed")
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    }

    @Test def testNestedObject {
    	val objectLiteral = """
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
    	val result = JSParser.parse(JSParser.jsobject,objectLiteral)

    	println()
    	println("result="+result+",class="+result.getClass())
    	result match { 
    	  	case JSParser.Success(jsobject,_) => println("SUCCESS jsobject="+jsobject)
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    	val jsobject = result match {
    	  		case JSParser.Success(jsobject,_) => jsobject
    	  		case JSParser.Failure(message,_) => throw new RuntimeException(message)
    		}
    }

}