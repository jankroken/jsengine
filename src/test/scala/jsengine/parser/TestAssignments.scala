package jsengine.parser

import org.junit.Test

import ParserTestSupport._
import jsengine.ast._

class TestAssignments {

    @Test def testAssignmentPlus() {
    	val source = "var a = b + c"
    	val ast = VariableDeclarations(List(
    				VariableDeclaration(JSIdentifier("a"),
    					Some(BinaryExpression(JSIdentifier("b"),List(
    					        BinaryExtension(Operator("+"),JSIdentifier("c"))))))))
    	verifyStatement(source,ast)
    }

    @Test def testAssignmentLogicalOr() {
    	val source = "var a = b || c"
    	val ast = VariableDeclarations(List(
    				VariableDeclaration(JSIdentifier("a"),
    					Some(BinaryExpression(JSIdentifier("b"),List(
    					        BinaryExtension(Operator("||"),JSIdentifier("c"))))))))
    	verifyStatement(source,ast)
    }
}