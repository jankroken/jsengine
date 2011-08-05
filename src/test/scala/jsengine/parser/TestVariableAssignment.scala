package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast._
import ParserTestSupport.verifySource

class TestVariableAssignment {

 @Test def testSimpleVariable() {
		val source = """
			var a
		"""
		val ast = JSSource(List(
				VariableDeclarations(List(
						VariableDeclaration(JSIdentifier("a"), None)
				))
		    ))
		verifySource(source,ast)
	}

  
  @Test def testSimpleVariableInitialization() {
		val source = """
			var a = 1
		"""
		val ast = JSSource(List(
				VariableDeclarations(List(
						VariableDeclaration(JSIdentifier("a"), Some(JSNumber("1")))
				))
		    ))
		verifySource(source,ast)
	}
  
  @Test def testFunction() {
		val source = """
			var a = function helloworld () { @NATIVECALL(helloworld) }
		"""
		val ast = JSSource(List(
				VariableDeclarations(List(
						VariableDeclaration(JSIdentifier("a"),
										   Some(JSFunction(Some(JSIdentifier("helloworld")),List(),List(JSNativeCall(JSIdentifier("helloworld"))))))
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
    			    VariableDeclaration(JSIdentifier("o"),
    			    	Some(JSLiteralObject(List(
    			    		(JSIdentifier("name"),JSLiteralObject(List(
    			    				(JSIdentifier("first"),JSString("Bruce")),
    			    				(JSIdentifier("last"),JSString("Springsteen"))
    			    		))),
    			    		(JSIdentifier("album"),JSFunction(Some(JSIdentifier("myAlbum")),List(),List(
    			    				JSString("The Darkness on the Edge of Town"),
    			    				JSNativeCall(JSIdentifier("favouritebrucespringsteenalbum"))
    			    		))),
    			    		(JSIdentifier("year"),JSNumber("1978")),
    			    		(JSNumber("1337") -> JSString("true"))
    			    	))
    			    ))
    			)),
    			JSFunction(Some(JSIdentifier("helloworld")),List(JSIdentifier("x")),List(JSNativeCall(JSIdentifier("helloworld")))),
				JSFunction(Some(JSIdentifier("byeworld")),List(JSIdentifier("x")),List(JSNativeCall(JSIdentifier("byeworld")))),
				JSNativeCall(JSIdentifier("helloworld"))
		))

    	verifySource(source,ast)

    }

    @Test def testSimpleAssignments {
    	val source = """
    			var x = 1, y, z = 2
    	"""
    	val ast = JSSource(List(
    			VariableDeclarations(List(
    					VariableDeclaration(JSIdentifier("x"),Some(JSNumber("1"))),
    					VariableDeclaration(JSIdentifier("y"),None),
    					VariableDeclaration(JSIdentifier("z"),Some(JSNumber("2")))
    			))
    	))
    	  
    	verifySource(source,ast)
    }

    
}