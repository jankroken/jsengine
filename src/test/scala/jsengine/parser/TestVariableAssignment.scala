package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSSource
import jsengine.ast.JSFunction
import jsengine.ast.JSNumber
import jsengine.ast.JSNativeCall
import jsengine.ast.JSUndefined
import jsengine.ast.JSLiteralObject
import jsengine.ast.VariableDeclarations
import jsengine.ast.VariableDeclaration
import jsengine.library.BuiltinObjects
import ParserTestSupport.verifySource

class TestVariableAssignment {

	@Test def testFunction() {
		val source = """
			var a = function helloworld () { @NATIVECALL(helloworld) }
		"""
		val ast = JSSource(List(
				VariableDeclarations(List(
						VariableDeclaration(JSString("a"),
										   JSFunction(Some(JSString("helloworld")),List(),List(JSNativeCall(JSString("helloworld")))))
				))
		    ))
		verifySource(source,ast)
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
  
    	  
    	val ast = JSSource(List(
    			VariableDeclarations(List(
    			    VariableDeclaration(JSString("o"),
    			    	JSLiteralObject(Map(
    			    		JSString("name") -> JSLiteralObject(Map(
    			    				JSString("first") -> JSString("Bruce"),
    			    				JSString("last") -> JSString("Springsteen")
    			    		)),
    			    		JSString("album") -> JSFunction(Some(JSString("myAlbum")),List(),List(
    			    				JSString("The Darkness on the Edge of Town"),
    			    				JSNativeCall(JSString("favouritebrucespringsteenalbum"))
    			    		)),
    			    		JSString("year") -> JSNumber("1978"),
    			    		JSString("1337") -> JSString("true")
    			    	))
    			    )
    			)),
    			JSFunction(Some(JSString("helloworld")),List(JSString("x")),List(JSNativeCall(JSString("helloworld")))),
				JSFunction(Some(JSString("byeworld")),List(JSString("x")),List(JSNativeCall(JSString("byeworld")))),
				JSNativeCall(JSString("helloworld"))
		))

    	verifySource(source,ast)
    }

    @Test def testSimpleAssignments {
    	val source = """
    			var x = 1, y, z = 2
    	"""
    	val ast = JSSource(List(
    			VariableDeclarations(List(
    					VariableDeclaration(JSString("x"),JSNumber("1")),
    					VariableDeclaration(JSString("y"),JSUndefined()),
    					VariableDeclaration(JSString("z"),JSNumber("2"))
    			))
    	))
    	  
    	verifySource(source,ast)
    }

    
}