package jsengine.rewriter

import org.junit.Test

import jsengine.ast._

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
