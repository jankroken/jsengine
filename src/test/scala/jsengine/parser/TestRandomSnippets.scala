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
    
    @Test def testW3SchoolsThrowFirstIfPart {
    	val source = """
    			if ( x > 10 ) {
    				throw "Err1"
    			} 
"""
    	val ast = JSSource(List(
    				IfStatement(
    					BinaryExpression(
    					    JSIdentifier("x"),
    					    List(BinaryExtension(Operator(">"),JSNumber("10")))),
    					JSBlock(List(ThrowStatement(JSString("Err1")))),
    					None)))
    	verifySource(source,ast)
    }

     @Test def testW3SchoolsThrowFullIfPart {
    	val source = """
    			if (x>10) {
    				throw "Err1"
    			} else if(x<0) {
    				throw "Err2"
    			} else if(isNaN(x)) {
    				throw "Err3"
    			}
    	"""
    	val ast = JSSource(List(
    				IfStatement(
    				    BinaryExpression(JSIdentifier("x"),List(BinaryExtension(Operator(">"),JSNumber("10")))),
    				    JSBlock(List(ThrowStatement(JSString("Err1")))),
    				    Some(
    				        IfStatement(
    				            BinaryExpression(JSIdentifier("x"),List(BinaryExtension(Operator("<"),JSNumber("0")))),
    				            JSBlock(List(ThrowStatement(JSString("Err2")))),
    				            Some(
    				                IfStatement(
    				                    CallExpression(0,JSIdentifier("isNaN"),List(ApplyArguments(List(JSIdentifier("x"))))),
    				                    JSBlock(List(ThrowStatement(JSString("Err3")))),
    				                    None)))))))
    	verifySource(source,ast)
    }
    
    @Test def testW3SchoolsThrow {
    	val source = """
    		var x=prompt("Enter a number between 0 and 10:","") ;
    		try {  
    			if (x>10) {
    				throw "Err1"
    			} else if(x<0) {
    				throw "Err2"
    			} else if(isNaN(x)) {
    				throw "Err3"
    			}
    		} catch(er) {
    			if(er=="Err1") {
    				alert("Error! The value is too high")
    			} ;
    			if(er=="Err2") {
    				alert("Error! The value is too low")
    			} ;
    			if(er=="Err3") {
    				alert("Error! The value is not a number")
    			}
    		}
    	"""
    	val ast = JSSource(List(
    			VariableDeclarations(List(
    			    VariableDeclaration(
    			        JSIdentifier("x"),
    			        Some(CallExpression(0,JSIdentifier("prompt"),List(
    			        		ApplyArguments(List(JSString("Enter a number between 0 and 10:"), JSString(""))))))))), 
    			    TryStatement(JSBlock(List(
    			        IfStatement(
    			            BinaryExpression(JSIdentifier("x"),List(BinaryExtension(Operator(">"),JSNumber("10")))),
    			            JSBlock(List(ThrowStatement(JSString("Err1")))),
    			            Some(IfStatement(
    			                BinaryExpression(JSIdentifier("x"),List(BinaryExtension(Operator("<"),JSNumber("0")))),
    			                JSBlock(List(ThrowStatement(JSString("Err2")))),
    			                Some(IfStatement(
    			                    CallExpression(0,JSIdentifier("isNaN"),List(ApplyArguments(List(JSIdentifier("x"))))),
    			                    JSBlock(List(ThrowStatement(JSString("Err3")))),None))))))),
    			   TryTail(
    			       Some(JSIdentifier("er")),
    			       Some(JSBlock(List(
    			           IfStatement(
    			               BinaryExpression(JSIdentifier("er"),List(BinaryExtension(Operator("=="),JSString("Err1")))),
    			               JSBlock(List(
    			                   CallExpression(0,JSIdentifier("alert"),List(
    			                       ApplyArguments(List(JSString("Error! The value is too high"))))))),
    			               None), 
    			           IfStatement(
    			               BinaryExpression(JSIdentifier("er"),List(BinaryExtension(Operator("=="),JSString("Err2")))),
    			               JSBlock(List(
    			                   CallExpression(0,JSIdentifier("alert"),List(
    			                       ApplyArguments(List(JSString("Error! The value is too low"))))))),
    			               None), 
    			          IfStatement(
    			              BinaryExpression(JSIdentifier("er"),List(BinaryExtension(Operator("=="),JSString("Err3")))),
    			              JSBlock(List(
    			                  CallExpression(0,JSIdentifier("alert"),List(
    			                      ApplyArguments(List(JSString("Error! The value is not a number"))))))),
    			               None)))),
    			      None))))
    	verifySource(source,ast)
    }
    
    @Test def testW3SchoolsForIn {
        val source = """
        	var person={fname:"John",lname:"Doe",age:25}; 

        	for (x in person) {
        		document.write(person[x] + " ")
        	}
        """
        val ast = JSSource(List(
        				VariableDeclarations(List(
        				    VariableDeclaration(JSIdentifier("person"),Some(
        				        JSLiteralObject(List(
        				            (JSIdentifier("fname"),JSString("John")), 
        				            (JSIdentifier("lname"),JSString("Doe")), 
        				            (JSIdentifier("age"),JSNumber("25")))))))), 
        				For(
        				    ForInit(JSIdentifier("x")),ForInUpdate(JSIdentifier("person")),
        				    JSBlock(List(
        				        CallExpression(0,JSIdentifier("document"),List(
        				            ApplyLookup(JSIdentifier("write")), 
        				            ApplyArguments(List(
        				                BinaryExpression(
        				                    CallExpression(0,JSIdentifier("person"),List(
        				                        ApplyLookup(JSIdentifier("x")))),
        				                    List(
        				                    	BinaryExtension(Operator("+"),JSString(" ")))))))))))))
        verifySource(source,ast)
    }

    @Test def testW3SchoolsBreakLoops {
        val source = """
        	var i=0;
        	for (i=0;i<=10;i++) {
        		if (i==3) {
        			break
        		};
        		document.write("The number is " + i);
        		document.write("<br />")
        	}
        """
        val ast = JSSource(List(
            VariableDeclarations(List(
                VariableDeclaration(JSIdentifier("i"),
                	Some(JSNumber("0"))))), 
                For(
                    ForInit(
                        AssignmentExpression(Operator("="),JSIdentifier("i"),JSNumber("0"))),
                    ForSemicolonUpdate(
                       Some(BinaryExpression(JSIdentifier("i"),List(
                            BinaryExtension(Operator("<="),JSNumber("10"))))),
                       Some(PostfixExpression(JSIdentifier("i"),Operator("++")))),
                    JSBlock(List(
                        IfStatement(
                            BinaryExpression(JSIdentifier("i"),List(BinaryExtension(Operator("=="),JSNumber("3")))),
                            JSBlock(List(
                                BreakStatement(None))),
                            None), 
                        CallExpression(0,JSIdentifier("document"),List(
                            ApplyLookup(JSIdentifier("write")), 
                            ApplyArguments(List(
                                BinaryExpression(JSString("The number is "),List(
                                    BinaryExtension(Operator("+"),JSIdentifier("i")))))))), 
                        CallExpression(0,JSIdentifier("document"),List(
                            ApplyLookup(JSIdentifier("write")), 
                            ApplyArguments(List(JSString("<br />"))))))))))
        verifySource(source,ast)
    }

    @Test def testW3SchoolsSwitch {
        val source = """
        	var d=new Date();
        	var theDay=d.getDay();

        	switch (theDay) {
        		case 5: 
        			document.write("Finally Friday");
        			break
        		case 6:
        			document.write("Super Saturday");
        			break
        		case 0:
        			document.write("Sleepy Sunday");
        			break
        		default:
        			document.write("I'm looking forward to this weekend!")
        	}
        """
        val ast = JSSource(List(
        				VariableDeclarations(List(
        				    VariableDeclaration(
        				        JSIdentifier("d"),
        				        Some(CallExpression(1,JSIdentifier("Date"),List(ApplyArguments(List()))))))), 
        				VariableDeclarations(List(
        				    VariableDeclaration(
        				        JSIdentifier("theDay"),
        				        Some(CallExpression(0,JSIdentifier("d"),List(ApplyLookup(JSIdentifier("getDay")), ApplyArguments(List()))))))), 
        				SwitchStatement(JSIdentifier("theDay"),List(
        				    LabeledCaseClause(JSNumber("5"),List(
        				        CallExpression(0,JSIdentifier("document"),List(
        				            ApplyLookup(JSIdentifier("write")), 
        				            ApplyArguments(List(JSString("Finally Friday"))))), 
        				        BreakStatement(None))), 
        				    LabeledCaseClause(JSNumber("6"),List(
        				        CallExpression(0,JSIdentifier("document"),List(
        				            ApplyLookup(JSIdentifier("write")), 
        				            ApplyArguments(List(JSString("Super Saturday"))))), 
        				        BreakStatement(None))), 
        				    LabeledCaseClause(JSNumber("0"),List(
        				        CallExpression(0,JSIdentifier("document"),List(
        				            ApplyLookup(JSIdentifier("write")), 
        				            ApplyArguments(List(JSString("Sleepy Sunday"))))), 
        				        BreakStatement(None))), 
        				    DefaultClause(List(
        				        CallExpression(0,JSIdentifier("document"),List(
        				            ApplyLookup(JSIdentifier("write")), 
        				            ApplyArguments(List(JSString("I'm looking forward to this weekend!")))))))))))
        				            
        verifySource(source,ast)
    }
    
}