package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSNumber
import jsengine.ast.JSSource
import jsengine.ast.JSObject
import jsengine.ast.JSFunction
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSNativeCall
import jsengine.ast.JSSourceElement


class TestJSSource {

	@Test def testTwoFunctions() {
		val functions = """
			function helloworld () { @NATIVECALL(helloworld) } ;
			function byeworld () { @NATIVECALL(byeworld) }
		"""
		val result = JSParser.parse(JSParser.program,functions)
		result match {
		  case JSParser.Success(source,_) => println("SUCCESS source="+source)
		  case JSParser.Failure(message,_) => fail(message)
		}
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
    	val result = JSParser.parse(JSParser.program,source)
    	println(result)
    	
    	result match { 
    	  	case JSParser.Success(jsobject,_) => println("SUCCESS jsobject="+jsobject)
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    	val jssource:JSSource = result match {
    	  		case JSParser.Success(jssource,_) => jssource
    	  		case JSParser.Failure(message,_) => throw new RuntimeException(message)
    		}
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
    	val result = JSParser.parse(JSParser.program,source)
    	val expected =
    	  	JSSource(List(
    	  			JSFunction(Some(JSString("outerObject")),List(JSString("foo")),List(
	    	  			JSLiteralObject(
		    	  			Map(JSString("name") -> JSLiteralObject(Map(
		    	  							JSString("first") -> JSString("Bruce"),
		    	  							JSString("last") -> JSString("Springsteen")
		    	  						  )),
		    	  				JSString("album") -> JSFunction(None,List(),List(
		    	  							JSLiteralObject(Map(
		    	  								JSString("album1") -> JSString("The Darkness on the Edge of Town")
		    	  							)),
		    	  							JSNativeCall(JSString("favouritebrucespringsteenalbum"))
		    	  						 )),
		    	  				JSString("year") -> JSNumber("1978"),
		    	  				JSString("1337") -> JSString("true")
		    	  			)
		    	  		),
		    	  		JSNativeCall(JSString("goodbyeworld"))
		    	  	))
	    	  	))
	    	  	
	    result match { 
    	  	case JSParser.Success(jssource,_) => {
    	  		println("3:SUCCESS jsobject="+jssource)
    	  		assertThat(jssource,is[Any](expected))
    	  	}
    	  	case JSParser.Failure(message,_) => fail("FAILURE message="+message)
    	}
    }

    
}