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

    @Test def testW3SchoolsDisplayDate {
    	val source = """
    			function displayDate()
    			{
    				document.getElementById("demo").innerHTML=Date()
    			}
    	"""
    	  val ast = JSSource(List(JSFunction(Some(JSIdentifier("displayDate")),List(),
    			  			 				 List(AssignmentExpression(Operator("="),
    			  			 						 				   CallExpression(0,JSIdentifier("document"),List(
    			  			 						 						   ApplyLookup(JSIdentifier("getElementById")), 
    			  			 						 						   ApplyArguments(List(JSString("demo"))), 
    			  			 						 						   ApplyLookup(JSIdentifier("innerHTML")))),
    			  			 						 				   CallExpression(0,JSIdentifier("Date"),List(ApplyArguments(List()))))))))
    	  verifySource(source,ast)
    }
    
    @Test def testW3SchoolsTryCatchExample {
    	val source = """
    		var txt="";
    		function message() {
    			try {
    				adddlert("Welcome guest!")
    			} catch(err) {
    				txt="There was an error on this page.\n\n";
    				txt+="Error description: " + err.description + "\n\n";
    				txt+="Click OK to continue.\n\n";
    				alert(txt)
    			}
    		}
    	"""
    	val ast = JSSource(List(
    				VariableDeclarations(List(
    					VariableDeclaration(JSIdentifier("txt"),Some(JSString(""))))), 
    				JSFunction(Some(JSIdentifier("message")),List(),List(
    				    TryStatement(
    				        JSBlock(List(
    				            CallExpression(0,JSIdentifier("adddlert"),List(ApplyArguments(List(JSString("Welcome guest!"))))))),
    				        TryTail(Some(JSIdentifier("err")),
    				                Some(JSBlock(List(
    				                    AssignmentExpression(
    				                        Operator("="),
    				                        JSIdentifier("txt"),
    				                        JSString("There was an error on this page.\n\n")), 
    				                    AssignmentExpression(
    				                    	Operator("+="),
    				                    	JSIdentifier("txt"),
    				                    	BinaryExpression(
    				                    	    JSString("Error description: "),
    				                    	    List(BinaryExtension(Operator("+"),
    				                    	    					 CallExpression(0,JSIdentifier("err"),List(ApplyLookup(JSIdentifier("description"))))), 
    				                    	    	 BinaryExtension(Operator("+"),
    				                    	    			 		JSString("\n\n"))))), 
    				                    AssignmentExpression(
    				                        Operator("+="),
    				                    	JSIdentifier("txt"),
    				                    	JSString("Click OK to continue.\n\n")), 
    				                    CallExpression(0,JSIdentifier("alert"),List(ApplyArguments(List(JSIdentifier("txt")))))))),
    				                None))))))
    				                    	    			 		    
    				                    	    			 		    
    	verifySource(source,ast)
    }
    
    
}