package jsengine.rewriter

import jsengine.ast.JSSource
import jsengine.runtime.tree.RTSource

object Rewriter {
	def rewrite(source: JSSource):RTSource =
    AST2RTRewriter.rewriteSource(
		  JSShrinkRewriter.rewriteSource(
	          FunctionDeclarationRewriter.rewriteSource(
	    	     JSShrinkRewriter.rewriteSource(
	    	         JSCallRewriter.rewriteSource(
	    	             JSInitialRewriter.rewriteSource(source))))))
}