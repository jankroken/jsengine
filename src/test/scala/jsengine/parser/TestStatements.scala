package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport.getASTOrFail
import ParserTestSupport.verifyFunction
import ParserTestSupport.verifyLiteralObject
import ParserTestSupport.verifyStatement
import jsengine.ast._

class TestStatements {

    @Test def testWhile {
    	val source = "while(true) { }"
    	val ast = While(JSIdentifier("true"),JSBlock(List()))
    	verifyStatement(source,ast)
    }

    @Test def testDoWhile {
    	val source = "do { a++ } while(true)"
    	val ast = DoWhile(JSBlock(List(PostfixExpression(JSIdentifier("a"),Operator("++")))),JSIdentifier("true"))
    	verifyStatement(source,ast)
    }
    
    @Test def testForIn {
    	val source = "for (n in { a: 1}) x++"
    	val ast = For(ForInit(JSIdentifier("n")),ForInUpdate(JSLiteralObject(List((JSIdentifier("a"),JSNumber("1"))))),PostfixExpression(JSIdentifier("x"),Operator("++")))
    	verifyStatement(source,ast)
    }

    @Test def testForSemicolon {
    	val source = "for (n + { a: 2}; n < z++ ; n++) x--"
    	val ast = For(ForInit(BinaryExpression(JSIdentifier("n"),List(BinaryExtension(Operator("+"),JSLiteralObject(List((JSIdentifier("a"),JSNumber("2")))))))),
    				  ForSemicolonUpdate(Some(BinaryExpression(JSIdentifier("n"),List(BinaryExtension(Operator("<"),PostfixExpression(JSIdentifier("z"),Operator("++")))))),
    						  			 Some(PostfixExpression(JSIdentifier("n"),Operator("++")))),
    				  PostfixExpression(JSIdentifier("x"),Operator("--")))
    	verifyStatement(source,ast)
    }
    
    @Test def testWith {
    	val source = "with (n < 2) x++"
    	val ast = WithStatement(BinaryExpression(JSIdentifier("n"),List(BinaryExtension(Operator("<"),JSNumber("2")))),PostfixExpression(JSIdentifier("x"),Operator("++")))
    	verifyStatement(source,ast)
    }


    @Test def testSwitch {
    	val source = """
    		switch(1) {
    			case 4+1 :
    				print("hello");
    				break;
    			case 5:
    				print("world");
    				break;
    			default:
    		}
    	"""
    	val ast = SwitchStatement(JSNumber("1"),
    							 List(LabeledCaseClause(BinaryExpression(JSNumber("4"),List(BinaryExtension(Operator("+"),JSNumber("1")))),
    									 				List(CallExpression(0,JSIdentifier("print"),List(ApplyArguments(List(JSString("hello"))))), 
    									 					 BreakStatement(None))), 
    								  LabeledCaseClause(JSNumber("5"),
    										  			List(CallExpression(0,JSIdentifier("print"),List(ApplyArguments(List(JSString("world"))))), 
    										  			     BreakStatement(None))), 
    								  DefaultClause(List())))
    	verifyStatement(source,ast)
    }

    @Test def testTry {
    	val source = """
    		try { print("hello") } catch(e) { print(e) } finally { print("bye") }
    	"""
    	val ast = TryStatement(JSBlock(List(CallExpression(0,JSIdentifier("print"),List(ApplyArguments(List(JSString("hello"))))))),
    			  TryTail(Some(JSIdentifier("e")),
    					  Some(JSBlock(List(CallExpression(0,JSIdentifier("print"),List(ApplyArguments(List(JSIdentifier("e")))))))),
    					  Some(JSBlock(List(CallExpression(0,JSIdentifier("print"),List(ApplyArguments(List(JSString("bye"))))))))))
    					  
    	verifyStatement(source,ast)
    }
    
    @Test def testIf {
       val source = """
    		if (true) false
       """
       val ast = IfStatement(JSIdentifier("true"),JSIdentifier("false"),None)
       verifyStatement(source,ast)
    }
    
    @Test def testTryIf {
       val source = """
    		try { if (true) false } catch(foo) {}
       """
       val ast = TryStatement(JSBlock(List(IfStatement(JSIdentifier("true"),
    		   										   JSIdentifier("false"),None))),
    		   				 TryTail(Some(JSIdentifier("foo")),
    		   				         Some(JSBlock(List())),
    		   				         None))
       verifyStatement(source,ast)
    }
    

}