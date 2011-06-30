package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSSource

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
    	jssource.evaluate
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

    	result match { 
    	  	case JSParser.Success(jsobject,_) => println("SUCCESS jsobject="+jsobject)
    	  	case JSParser.Failure(message,_) => println("FAILURE message="+message)
    	}
    	val jsobject = result match {
    	  		case JSParser.Success(jsobject,_) => jsobject
    	  		case JSParser.Failure(message,_) => throw new RuntimeException(message)
    		}
    	jsobject.evaluate
    }

    
}