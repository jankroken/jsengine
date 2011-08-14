package jsengine.rewriter

import org.junit.Test

import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail
// import ParserTestSupport._
import jsengine.parser.JSParser
import jsengine.ast._
import scala.io.Source

class TestInitialRewriter {

     @Test def testVariableDeclaration() {
    	val source = JSSource(List(VariableDeclarations(List(VariableDeclaration(JSIdentifier("a"),Some(JSNumber("1")))))))
    	val rewritten = JSInitialRewriter.rewriteSource(source)
    	println(rewritten)
     }

     @Test def testVariableDeclarationStep2() {
    	val source = JSSource(List(Declare(JSIdentifier("a")), Assign(JSIdentifier("a"),JSNumber("1"))))
    	val rewritten = JSCallRewriter.rewriteSource(source)
    	println(rewritten)
     }

     @Test def testVariableDeclarationStep3() {
    	val source = JSSource(List(Declare(JSIdentifier("a")), Assign(JSIdentifier("a"),JSNumber("1"))))
    	val rewritten = JSShrinkRewriter.rewriteSource(source)
    	println(rewritten)
     }
     
     
     @Test def testVariableDeclarations() {
    	val source = JSSource(List(VariableDeclarations(List(VariableDeclaration(JSIdentifier("a"),None),VariableDeclaration(JSIdentifier("b"),None)))))
    	val rewritten = JSInitialRewriter.rewriteSource(source)
    	println(rewritten)
     }

     

}
