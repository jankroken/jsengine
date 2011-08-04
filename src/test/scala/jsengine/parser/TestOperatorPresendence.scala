package jsengine.parser

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
import ParserTestSupport._
import jsengine.ast._

class testOperatorPresendence {

    @Test def testSimplePlus {
    	val source = "1 + 2"
    	val ast = BinaryExpression(JSNumber("1"), List(BinaryExtension(Operator("+"),JSNumber("2"))))
    	verifyExpression(source,ast)
    }

    @Test def testPlusAndMultiply {
    	val source = "1 + 2 * 3"
    	val ast = BinaryExpression(JSNumber("1"), List(
    				BinaryExtension(Operator("+"),
    								BinaryExpression(JSNumber("2"),
    								List(BinaryExtension(Operator("*"),JSNumber("3")))))))
    	verifyExpression(source,ast)
    }

    @Test def testPostfix {
    	val source = "1++"
    	val ast = PostfixExpression(JSNumber("1"), Operator("++"))
    	verifyExpression(source,ast)
    }

    @Test def testUnary {
    	val source = "++ 1"
    	val ast = UnaryExpression(List(Operator("++")),JSNumber("1"))
    	verifyExpression(source,ast)
    }
    
    @Test def testDoubleUnary {
        val source = "!!1"
        val ast = UnaryExpression(List(Operator("!"),Operator("!")),JSNumber("1"))
        verifyExpression(source,ast)
    }

    @Test def testShift {
    	val source = "++1 >> 2"
    	val ast = BinaryExpression(UnaryExpression(List(Operator("++")),JSNumber("1")),List(BinaryExtension(Operator(">>"),JSNumber("2"))))
    	verifyExpression(source,ast)
    }

    @Test def testRelational {
    	val source = "4 > 1 >> 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator(">"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator(">>"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testEqualityl {
    	val source = "4 == 1 > 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("=="),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator(">"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testBinaryAnd {
    	val source = "4 & 1 == 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("&"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator("=="),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testBinaryXor {
    	val source = "4 ^ 1 & 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("^"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator("&"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testBinaryOr {
    	val source = "4 | 1 ^ 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("|"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator("^"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testLogicalAnd {
    	val source = "4 && 1 | 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("&&"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator("|"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }

    @Test def testLogicalOr {
    	val source = "4 || 1 && 2"
    	val ast = BinaryExpression(JSNumber("4"),
    							  List(BinaryExtension(Operator("||"),
    							                      BinaryExpression(JSNumber("1"),
    							                                       List(BinaryExtension(Operator("&&"),JSNumber("2")))))))
    	verifyExpression(source,ast)
    }
    
    @Test def testConditional {
    	val source = "1 ? 2 : 3"
    	val ast = ConditionalExpression(JSNumber("1"),JSNumber("2"),JSNumber("3"))
    	verifyExpression(source,ast)
    }

    @Test def testDoubleConditional {
    	val source = "a ? b ? c : d : e "
    	val ast = ConditionalExpression(JSIdentifier("a"),ConditionalExpression(JSIdentifier("b"),JSIdentifier("c"),JSIdentifier("d")),JSIdentifier("e"))
    	verifyExpression(source,ast)
    }

    @Test def testAssignWithConditional {
    	val source = "a = b ? c : d "
    	val ast = AssignmentExpression(Operator("="),JSIdentifier("a"),ConditionalExpression(JSIdentifier("b"),JSIdentifier("c"),JSIdentifier("d")))
    	verifyExpression(source,ast)
    }

    @Test def testConditionalWithAssignment {
    	val source = "a ? b = c ? d : e : f"
    	val ast = ConditionalExpression(JSIdentifier("a"),AssignmentExpression(Operator("="),JSIdentifier("b"),ConditionalExpression(JSIdentifier("c"),JSIdentifier("d"),JSIdentifier("e"))),JSIdentifier("f"))
    	verifyExpression(source,ast)
    }
}