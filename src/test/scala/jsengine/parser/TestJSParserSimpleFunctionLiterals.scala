package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.getASTOrFail
import jsengine.ast.JSFunction
import jsengine.ast.JSNativeCall
import jsengine.ast.ASTNode

import jsengine.ast.JSString

class TestJSParserSimpleFunctionLiterals {

	private def verifyFunction(source: String, expected: ASTNode) {
		ParserTestSupport.verifyParsing[JSFunction](JSParser.functionExpression,source,expected)
	}
  
    @Test def testEmptyFunction {
    	val source = "function () {}"
    	val ast = JSFunction(None,List(),List())
    	verifyFunction(source,ast)
    }
    
    @Test def testEmptyNamedFunction {
    	val source = "function hello () {}"
    	val ast = JSFunction(Some(JSString("hello")),List(),List())
    	verifyFunction(source,ast)
    }

    @Test def testOneArgumentEmptyFunction {
    	val source = "function (x) {}"
    	val ast = JSFunction(None,List(JSString("x")),List())
    	verifyFunction(source,ast)
    }

    @Test def testTwoArgumentsEmptyFunction {
    	val source = "function (x,y) {}"
    	val ast = JSFunction(None,List(JSString("x"),JSString("y")),List())
    	verifyFunction(source,ast)
    }
    
    @Test def testNativeCall {
    	val source = "@NATIVECALL(helloworld)"
    	val ast = JSNativeCall(JSString("helloworld"))
		ParserTestSupport.verifyParsing[JSNativeCall](JSParser.nativeCall,source,ast)
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