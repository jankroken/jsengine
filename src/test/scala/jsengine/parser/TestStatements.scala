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
    	val ast = While(JSString("true"),JSBlock(List()))
    	verifyStatement(source,ast)
    }

    @Test def testDoWhile {
    	val source = "do { a++ } while(true)"
    	val ast = DoWhile(JSBlock(List(PostfixExpression(JSString("a"),Operator("++")))),JSString("true"))
    	verifyStatement(source,ast)
    }
    
    @Test def testForIn {
    	val source = "for (n in { a: 1}) x++"
    	val ast = For(ForInit(JSString("n")),ForInUpdate(JSLiteralObject(List((JSString("a"),JSNumber("1"))))),PostfixExpression(JSString("x"),Operator("++")))
    	verifyStatement(source,ast)
    }

    @Test def testForSemicolon {
    	val source = "for (n + { a: 2}; n < z++ ; n++) x--"
    	val ast = For(ForInit(BinaryExpression(JSString("n"),List(BinaryExtension(Operator("+"),JSLiteralObject(List((JSString("a"),JSNumber("2")))))))),
    				  ForSemicolonUpdate(Some(BinaryExpression(JSString("n"),List(BinaryExtension(Operator("<"),PostfixExpression(JSString("z"),Operator("++")))))),
    						  			 Some(PostfixExpression(JSString("n"),Operator("++")))),
    				  PostfixExpression(JSString("x"),Operator("--")))
    	verifyStatement(source,ast)
    }
    
    @Test def testWith {
    	val source = "with (n < 2) x++"
    	val ast = WithStatement(BinaryExpression(JSString("n"),List(BinaryExtension(Operator("<"),JSNumber("2")))),PostfixExpression(JSString("x"),Operator("++")))
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
    									 				List(CallExpression(0,JSString("print"),List(ApplyArguments(List(JSString("hello"))))), 
    									 					 BreakStatement(None))), 
    								  LabeledCaseClause(JSNumber("5"),
    										  			List(CallExpression(0,JSString("print"),List(ApplyArguments(List(JSString("world"))))), 
    										  			     BreakStatement(None))), 
    								  DefaultClause(List())))
    	verifyStatement(source,ast)
    }


}