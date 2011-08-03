package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject

import jsengine.ast._
import ParserTestSupport.verifySource

class TestJSSource {

	@Test def testTwoFunctions() {
		val source = """
			function helloworld () { @NATIVECALL(helloworld) } ;
			function byeworld () { @NATIVECALL(byeworld) }
		"""

		val ast = JSSource(List(JSFunction(Some(JSIdentifier("helloworld")),List(),List(JSNativeCall(JSIdentifier("helloworld")))),
				       			JSFunction(Some(JSIdentifier("byeworld")),List(),List(JSNativeCall(JSIdentifier("byeworld"))))))	
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
    			    (JSIdentifier("name"),JSLiteralObject(List(
    			    		(JSIdentifier("first"),JSString("Bruce")),
    			    		(JSIdentifier("last"),JSString("Springsteen"))
    			    ))),
    			    (JSIdentifier("album"),JSFunction(Some(JSIdentifier("myAlbum")),List(),List(
    			    		JSString("The Darkness on the Edge of Town"),
    			    		JSNativeCall(JSIdentifier("favouritebrucespringsteenalbum"))
    			    ))),
    			    (JSIdentifier("year"),JSNumber("1978")),
    			    (JSNumber("1337"),JSString("true"))
    			)),
    			JSFunction(Some(JSIdentifier("helloworld")),List(JSIdentifier("x")),List(JSNativeCall(JSIdentifier("helloworld")))),
				JSFunction(Some(JSIdentifier("byeworld")),List(JSIdentifier("x")),List(JSNativeCall(JSIdentifier("byeworld")))),
				JSNativeCall(JSIdentifier("helloworld"))
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
    	  			JSFunction(Some(JSIdentifier("outerObject")),List(JSIdentifier("foo")),List(
	    	  			JSLiteralObject(
		    	  			List((JSIdentifier("name"),JSLiteralObject(List(
		    	  							(JSIdentifier("first"),JSString("Bruce")),
		    	  							(JSIdentifier("last"),JSString("Springsteen"))
		    	  						  ))),
		    	  				(JSIdentifier("album"),JSFunction(None,List(),List(
		    	  							JSLiteralObject(List(
		    	  								(JSIdentifier("album1"),JSString("The Darkness on the Edge of Town"))
		    	  							)),
		    	  							JSNativeCall(JSIdentifier("favouritebrucespringsteenalbum"))
		    	  						 ))),
		    	  				(JSIdentifier("year"),JSNumber("1978")),
		    	  				(JSNumber("1337"),JSString("true"))
		    	  			)
		    	  		),
		    	  		JSNativeCall(JSIdentifier("goodbyeworld"))
		    	  	))
	    	  	))

	    verifySource(source,ast)
    }

}