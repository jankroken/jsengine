package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString

class TestJSParserSimpleFunctionLiterals {

    @Test def testEmptyFunction {
    	val result = JSParser.parse(JSParser.functionExpression,"function () {}")
    	result match {
    	  case JSParser.Success(jsfunction,_) => println("Success: "+jsfunction)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }
    
    @Test def testEmptyNamedFunction {
    	val result = JSParser.parse(JSParser.functionExpression,"function hello () {}")
    	result match {
    	  case JSParser.Success(jsfunction,_) => println("Success: "+jsfunction)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }

    @Test def testOneArgumentEmptyFunction {
    	val result = JSParser.parse(JSParser.functionExpression,"function (x) {}")
    	result match {
    	  case JSParser.Success(jsfunction,_) => println("Success: "+jsfunction)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }

    @Test def testTwoArgumentsEmptyFunction {
    	val result = JSParser.parse(JSParser.functionExpression,"function (x,y) {}")
    	result match {
    	  case JSParser.Success(jsfunction,_) => println("Success: "+jsfunction)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }
    
    @Test def testNativeCall {
    	val result = JSParser.parse(JSParser.nativeCall,"@NATIVECALL(helloworld)")
    	result match {
    	  case JSParser.Success(nativecall,_) => println("Success: "+nativecall)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }

    @Test def testFunctionNativeCalls {
    	val result = JSParser.parse(JSParser.functionExpression,"function f (x) { @NATIVECALL(hello) ; @NATIVECALL(world) }")
    	result match {
    	  case JSParser.Success(jsfunction,_) => println("Success: "+jsfunction)
    	  case JSParser.Failure(message,_) => fail("Parsing failed: "+message)
    	}
    }

    @Test def testSimpleObjectWithFunction {
    	val objectLiteral = """
    		{
    			name : function myName() { @NATIVECALL(hello) }
    		}
    		"""
    	val result = JSParser.parse(JSParser.jsobject,objectLiteral)

    	result match { 
    	  	case JSParser.Success(jsobject,_) => println("SUCCESS jsobject="+jsobject)
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    	val jsobject = result match {
    	  		case JSParser.Success(jsobject,_) => jsobject
    	  		case JSParser.Failure(message,_) => throw new RuntimeException(message)
    		}
    }


    @Test def testNestedObject {
    	val objectLiteral = """
    		{
    			name : {
    				first : "Bruce",
    				last : "Springsteen"
    			},
    			album : function myAlbum () { 
    						"The Darkness on the Edge of Town" ; 
    						@NATIVECALL(favouritebrucespringsteenalbum) 
    					},
    			year: 1978,
    			1337 : "true"
    		 }
    	"""
    	val result = JSParser.parse(JSParser.jsobject,objectLiteral)

    	result match { 
    	  	case JSParser.Success(jsobject,_) => println("SUCCESS jsobject="+jsobject)
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    	val jsobject = result match {
    	  		case JSParser.Success(jsobject,_) => jsobject
    	  		case JSParser.Failure(message,_) => throw new RuntimeException(message)
    		}
    }

    @Test def testNestedObjectsAndFunctions {
    	val objectLiteral = """
    		function outerObject (foo) {
    			{
    				name : {
    					first : "Bruce",
    					last : "Springsteen"
    				},
    				album : function () {
    							{ album1 : "The Darkness on the Edge of Town" } ; 
    							@NATIVECALL(favouritebrucespringsteenalbum) 
    						},
    				year: 1978,
    				1337 : "true"
    		 	} ;
    			@NATIVECALL(goodbyeworld)
    		}
    	"""
    	val result = JSParser.parse(JSParser.functionExpression,objectLiteral)

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