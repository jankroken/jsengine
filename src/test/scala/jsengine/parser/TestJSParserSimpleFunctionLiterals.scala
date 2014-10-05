package jsengine.parser

import org.junit.Test

import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject
import jsengine.ast._

class TestJSParserSimpleFunctionLiterals {

  
    @Test def testEmptyFunction() {
    	val source = "function () {}"
    	val ast = JSFunction(None,List(),List())
    	verifyFunction(source,ast)
    }
    @Test def testEmptyNamedFunction() {
    	val source = "function hello () {}"
    	val ast = JSFunction(Some(JSIdentifier("hello")),List(),List())
    	verifyFunction(source,ast)
    }

    @Test def testOneArgumentEmptyFunction() {
    	val source = "function (x) {}"
    	val ast = JSFunction(None,List(JSIdentifier("x")),List())
    	verifyFunction(source,ast)
    }

    @Test def testTwoArgumentsEmptyFunction() {
    	val source = "function (x,y) {}"
    	val ast = JSFunction(None,List(JSIdentifier("x"),JSIdentifier("y")),List())
    	verifyFunction(source,ast)
    }
    
    @Test def testNativeCall() {
    	val source = "@NATIVECALL(helloworld)"
    	val ast = JSNativeCall(JSIdentifier("helloworld"))
		ParserTestSupport.verifyParsing[JSNativeCall](JSParser.nativeCall,source,ast)
    }

    @Test def testFunctionNativeCalls() {
    	val source = "function f (x) { @NATIVECALL(hello) ; @NATIVECALL(world) }"
    	val ast = JSFunction(Some(JSIdentifier("f")),List(JSIdentifier("x")),List(JSNativeCall(JSIdentifier("hello")),JSNativeCall(JSIdentifier("world"))))
    	verifyFunction(source,ast)
    }

    @Test def testSimpleObjectWithFunction() {
    	val source = """
    		{
    			name : function myName() { @NATIVECALL(hello) }
    		}
    		"""
    	val ast = JSLiteralObject(List(
    			(JSString("name"),JSFunction(Some(JSIdentifier("myName")),List(),List(JSNativeCall(JSIdentifier("hello")))))
    	))  
    	
    	verifyLiteralObject(source,ast)
    }


    @Test def testNestedObject() {
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
    				(JSString("album"),JSFunction(Some(JSIdentifier("myAlbum")),List(),List(
    										 JSString("The Darkness on the Edge of Town"),
    										 JSNativeCall(JSIdentifier("favouritebrucespringsteenalbum"))
    									 ))),
    				(JSString("year"),JSNumber("1978")),
    				(JSNumber("1337"),JSString("true"))
    			))

    	verifyLiteralObject(source,ast)
    }

    @Test def testNestedObjectsAndFunctions() {
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
    	  JSFunction(Some(JSIdentifier("outerObject")),List(JSIdentifier("foo")),List(
    			  JSLiteralObject(List(
    				(JSString("name"),JSLiteralObject(List(
    				    (JSString("first"),JSString("Bruce")),
    				    (JSString("last"),JSString("Springsteen"))
    				))),
    				(JSString("album"),JSFunction(None,List(),List(
    										 JSLiteralObject(List(
    												 (JSString("album1"),JSString("The Darkness on the Edge of Town"))
    										 )),
    										 JSNativeCall(JSIdentifier("favouritebrucespringsteenalbum"))
    									 ))),
    				(JSString("year"),JSNumber("1978")),
    				(JSNumber("1337"),JSString("true"))
    			  )),
    			  JSNativeCall(JSIdentifier("goodbyeworld"))
    	  ))

    	verifyFunction(source,ast)
    }

    
}