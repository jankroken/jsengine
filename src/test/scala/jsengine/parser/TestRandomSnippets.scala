package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport._
import jsengine.ast._

class TestRandomSnippets {
  
  
    @Test def testSimpleCall {
    	val source = """
    			foo('Date: ')
    	"""
    	val ast = JSSource(List(CallExpression(0,JSIdentifier("foo"),List(ApplyArguments(List(JSString("Date: ")))))))
    	verifySource(source,ast)
    }

    @Test def testWhile {
    	val source = """
			function PrintDate() {
    			today = new Date() ;
    			document.write('Date: ', today.getMonth()+1, '/', today.getDate(), '/', today.getYear())
    		}    	
    	"""
    	val ast = JSFunction(Some(JSIdentifier("PrintDate")),List(),
    						 List(AssignmentExpression(Operator("="),JSIdentifier("today"),CallExpression(1,JSIdentifier("Date"),List(ApplyArguments(List())))), 
    						      CallExpression(0,JSIdentifier("document"),List(ApplyLookup(JSIdentifier("write")), ApplyArguments(List(JSString("Date: "), BinaryExpression(CallExpression(0,JSIdentifier("today"),List(ApplyLookup(JSIdentifier("getMonth")), ApplyArguments(List()))),List(BinaryExtension(Operator("+"),JSNumber("1")))), JSString("/"), CallExpression(0,JSIdentifier("today"),List(ApplyLookup(JSIdentifier("getDate")), ApplyArguments(List()))), JSString("/"), CallExpression(0,JSIdentifier("today"),List(ApplyLookup(JSIdentifier("getYear")), ApplyArguments(List())))))))))
    	verifyFunction(source,ast)
    }

    @Test def testFor {
    	val source = """
    		for (var i = 0; i < whitespace.length; ++i)
    		{
    			var v = whitespace[i] ;
    			reportCompare(true, !!(/\\s/.test(v.s)), 'Is ' + v.t + ' a space')
    		}
    	"""
    	val ast = For(ForInit(VariableDeclarations(List(VariableDeclaration(JSIdentifier("i"),Some(JSNumber("0")))))),
    				  ForSemicolonUpdate(Some(BinaryExpression(JSIdentifier("i"),List(BinaryExtension(Operator("<"),CallExpression(0,JSIdentifier("whitespace"),List(ApplyLookup(JSIdentifier("length")))))))),
    						  			 Some(UnaryExpression(List(Operator("++")),JSIdentifier("i")))),
    				  JSBlock(List(VariableDeclarations(List(VariableDeclaration(JSIdentifier("v"),Some(CallExpression(0,JSIdentifier("whitespace"),List(ApplyLookup(JSIdentifier("i")))))))), 
    				          CallExpression(0,JSIdentifier("reportCompare"),List(
    				              ApplyArguments(List(JSIdentifier("true"), 
    				        		  				  UnaryExpression(List(Operator("!"), Operator("!")),
    				        		  				 				  CallExpression(0,JSRegexLiteral("/\\\\s/"),List(
    				        		  				 				      ApplyLookup(JSIdentifier("test")), 
    				        		  				 					  ApplyArguments(List(
    				        		  				 					      CallExpression(0,JSIdentifier("v"),List(ApplyLookup(JSIdentifier("s"))))))))), 
    				        		  				  BinaryExpression(JSString("Is "),
    				        		  				 				  List(BinaryExtension(
    				        		  				 						   Operator("+"),
    				        		  				 						   CallExpression(0,JSIdentifier("v"),List(
    				        		  				 					           ApplyLookup(JSIdentifier("t"))))), 
    				        		  				 					   BinaryExtension(
    				        		  				 					       Operator("+"),
    				        		  				 					       JSString(" a space")))))))))))    	
    				        		  				 						  	            
        verifyStatement(source,ast)
    }

    
}