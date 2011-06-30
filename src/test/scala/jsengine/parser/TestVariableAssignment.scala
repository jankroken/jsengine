package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSSource

class TestVariableAssignment {

	@Test def testFunction() {
		val functions = """
			var a = function helloworld () { @NATIVECALL(helloworld) }
		"""
		val result = JSParser.parse(JSParser.program,functions)
		result match {
		  case JSParser.Success(source,_) => println("SUCCESS source="+source)
		  case JSParser.Failure(message,_) => fail(message)
		}
	}
  
  
    @Test def testNestedObject {
    	val source = """
    		var o = {
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

    @Test def testSimpleAssignments {
    	val source = """
    			var x = 1, y, z = 2
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
    }

    
}