package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject

import jsengine.ast.JSString
import jsengine.ast.JSNumber
import jsengine.ast.JSSource
import jsengine.ast.JSObject
import jsengine.ast.JSFunction
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSNativeCall
import jsengine.ast.JSSourceElement
import ParserTestSupport.verifySource

class TestJSSource {

	@Test def testTwoFunctions() {
		val source = """
			function helloworld () { @NATIVECALL(helloworld) } ;
			function byeworld () { @NATIVECALL(byeworld) }
		"""
		val ast = JSSource(List(JSFunction(Some(JSString("helloworld")),List(),List(JSNativeCall(JSString("helloworld")))),
				       			JSFunction(Some(JSString("byeworld")),List(),List(JSNativeCall(JSString("byeworld"))))))	
		verifySource(source,ast)
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
    		 } ;
    		function helloworld(x) { @NATIVECALL(helloworld) } ;
    		function byeworld(x) { @NATIVECALL(byeworld) } ;
    		@NATIVECALL(helloworld)
    	"""
    	  
    	val ast = JSSource(List(
    			JSLiteralObject(List(
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
    			)),
    			JSFunction(Some(JSString("helloworld")),List(JSString("x")),List(JSNativeCall(JSString("helloworld")))),
				JSFunction(Some(JSString("byeworld")),List(JSString("x")),List(JSNativeCall(JSString("byeworld")))),
				JSNativeCall(JSString("helloworld"))
		))
    	  
		verifySource(source,ast)
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
    	  	JSSource(List(
    	  			JSFunction(Some(JSString("outerObject")),List(JSString("foo")),List(
	    	  			JSLiteralObject(
		    	  			List((JSString("name"),JSLiteralObject(List(
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
		    	  			)
		    	  		),
		    	  		JSNativeCall(JSString("goodbyeworld"))
		    	  	))
	    	  	))

	    verifySource(source,ast)
    }

    // temporary test, to be removed
    @Test
    def testNot() {
      val source = "if()"
      val x = JSParser.parse(JSParser.testNot,source)
      println(x)
    }
    
}