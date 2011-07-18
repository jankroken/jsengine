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
import jsengine.ast.JSFunction
import jsengine.ast.JSNativeCall
import jsengine.ast.ASTNode
import jsengine.ast.JSString
import jsengine.ast.JSNumber
import jsengine.ast.JSArrayLiteral
import jsengine.ast.JSLiteralObject

class TestArrayLiterals {

  
    @Test def testSimpleArray {
    	val source = "[1]"
    	val ast = JSArrayLiteral(List(Some(JSNumber("1"))))
    	verifyArrayLiteral(source,ast)
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
    	val source = "function f (x) { @NATIVECALL(hello) ; @NATIVECALL(world) }"
    	val ast = JSFunction(Some(JSString("f")),List(JSString("x")),List(JSNativeCall(JSString("hello")),JSNativeCall(JSString("world"))))
    	verifyFunction(source,ast)
    }

    @Test def testSimpleObjectWithFunction {
    	val source = """
    		{
    			name : function myName() { @NATIVECALL(hello) }
    		}
    		"""
    	val ast = JSLiteralObject(List(
    			(JSString("name"),JSFunction(Some(JSString("myName")),List(),List(JSNativeCall(JSString("hello")))))
    	))  
    	
    	verifyLiteralObject(source,ast)
    }


    @Test def testNestedObject {
    	val source = """
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
    	val ast = JSLiteralObject(List(
    				(JSString("name"),JSLiteralObject(List(
    				    (JSString("first"),JSString("Bruce")),
    				    (JSString("last"),JSString("Springsteen"))
    				))),
    				(JSString("album"),JSFunction(Some(JSString("myAlbum")),List(),List(
    										 JSString("The Darkness on the Edge of Town"),
    										 JSNativeCall(JSString("favouritebrucespringsteenalbum"))
    									 ))),
    				(JSString("year"),JSNumber("1978")),
    				(JSNumber("1337"),JSString("true"))
    			))

    	verifyLiteralObject(source,ast)
    }

    @Test def testNestedObjectsAndFunctions {
    	val source = """
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
    	  
    	val ast =
    	  JSFunction(Some(JSString("outerObject")),List(JSString("foo")),List(
    			  JSLiteralObject(List(
    				(JSString("name"),JSLiteralObject(List(
    				    (JSString("first"),JSString("Bruce")),
    				    (JSString("last"),JSString("Springsteen"))
    				))),
    				(JSString("album"),JSFunction(None,List(),List(
    										 JSLiteralObject(List(
    												 (JSString("album1"),JSString("The Darkness on the Edge of Town"))
    										 )),
    										 JSNativeCall(JSString("favouritebrucespringsteenalbum"))
    									 ))),
    				(JSString("year"),JSNumber("1978")),
    				(JSNumber("1337"),JSString("true"))
    			  )),
    			  JSNativeCall(JSString("goodbyeworld"))
    	  ))

    	verifyFunction(source,ast)
    }

    
}