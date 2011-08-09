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

class TestRewriter {

  
  
     def testFile(filename: String) {
    	val result = JSParser.parse(JSParser.source,Source.fromURL(this.getClass().getClassLoader().getResource(filename)).reader)
    	println(result)
    	result match {
    	  case JSParser.Success(ast,_) => println(JSCallRewriter.rewriteSource(JSInitialRewriter.rewriteSource(ast)))
    	  case JSParser.NoSuccess(message,src) => println("failed")
    	}

    }
     
     
     @Test def testPart1() {
       testFile("jquery_part1.js")
     }
}



JSSource(List(
	Declare(JSIdentifier(document)), 
	Assign(JSIdentifier(document),Lookup(JSIdentifier(window),JSString(document))), 
	Declare(JSIdentifier(navigator)), 
	Assign(JSIdentifier(navigator),Lookup(JSIdentifier(window),JSString(navigator))), 
	Declare(JSIdentifier(location)), 
	Assign(JSIdentifier(location),Lookup(JSIdentifier(window),JSString(location))), 
	Declare(JSIdentifier(jQuery)), 
	Assign(
	    JSIdentifier(jQuery),
	    JSFunction(None,List(),List(
	        Declare(JSIdentifier(jQuery)), 
	        Assign(
	            JSIdentifier(jQuery),
	            JSFunction(None,List(JSIdentifier(selector), JSIdentifier(context)),List(
	                ReturnStatement(Some(
	                    New(
	                        Lookup(
	                            Lookup(JSIdentifier(jQuery),JSString(fn)),
	                            JSString(init)),
	                        List(JSIdentifier(selector), JSIdentifier(context), JSIdentifier(rootjQuery)))))))), 
	        Declare(JSIdentifier(_jQuery)), 
	        Assign(JSIdentifier(_jQuery),Lookup(JSIdentifier(window),JSString(jQuery))), 
	        Declare(JSIdentifier(_$)), 
	        Assign(JSIdentifier(_$),Lookup(JSIdentifier(window),JSString($))), 
	        Declare(JSIdentifier(rootjQuery)), 
	        Declare(JSIdentifier(quickExpr)), 
	        Assign(JSIdentifier(quickExpr),JSRegexLiteral(/^(?:[^<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/)), 
	        Declare(JSIdentifier(rnotwhite)), 
	        Assign(JSIdentifier(rnotwhite),JSRegexLiteral(/\S/)), 
	        Declare(JSIdentifier(trimLeft)), 
	        Assign(JSIdentifier(trimLeft),JSRegexLiteral(/^\s+/)), 
	        Declare(JSIdentifier(trimRight)), 
	        Assign(JSIdentifier(trimRight),JSRegexLiteral(/\s+$/)), 
	        Declare(JSIdentifier(rdigit)), 
	        Assign(JSIdentifier(rdigit),JSRegexLiteral(/\d/)), 
	        Declare(JSIdentifier(rsingleTag)), 
	        Assign(JSIdentifier(rsingleTag),JSRegexLiteral(/^<(\w+)\s*\/?>(?:<\/\1>)?$/)), 
	        Declare(JSIdentifier(rvalidchars)), 
	        Assign(JSIdentifier(rvalidchars),JSRegexLiteral(/^[\],:{}\s]*$/)), 
	        Declare(JSIdentifier(rvalidescape)), 
	        Assign(JSIdentifier(rvalidescape),JSRegexLiteral("/\\(?:[\\\/bfnrt]|u[0-9a-fA-F]{4})/g")), Declare(JSIdentifier(rvalidtokens)), Assign(JSIdentifier(rvalidtokens),JSRegexLiteral(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g)), Declare(JSIdentifier(rvalidbraces)), Assign(JSIdentifier(rvalidbraces),JSRegexLiteral(/(?:^|:|,)(?:\s*\[)+/g)), Declare(JSIdentifier(rwebkit)), Assign(JSIdentifier(rwebkit),JSRegexLiteral(/(webkit)[ \/]([\w.]+)/)), Declare(JSIdentifier(ropera)), Assign(JSIdentifier(ropera),JSRegexLiteral(/(opera)(?:.*version)?[ \/]([\w.]+)/)), Declare(JSIdentifier(rmsie)), Assign(JSIdentifier(rmsie),JSRegexLiteral(/(msie) ([\w.]+)/)), Declare(JSIdentifier(rmozilla)), Assign(JSIdentifier(rmozilla),JSRegexLiteral(/(mozilla)(?:.*? rv:([\w.]+))?/)), Declare(JSIdentifier(rdashAlpha)), Assign(JSIdentifier(rdashAlpha),JSRegexLiteral(/-([a-z])/ig)), Declare(JSIdentifier(fcamelCase)), Assign(JSIdentifier(fcamelCase),JSFunction(None,List(JSIdentifier(all), JSIdentifier(letter)),List(ReturnStatement(Some(Call(Lookup(JSIdentifier(letter),JSString(toUpperCase)),List())))))), Declare(JSIdentifier(userAgent)), Assign(JSIdentifier(userAgent),Lookup(JSIdentifier(navigator),JSString(userAgent))), Declare(JSIdentifier(browserMatch)), Declare(JSIdentifier(readyList)), Declare(JSIdentifier(DOMContentLoaded)), Declare(JSIdentifier(toString)), Assign(JSIdentifier(toString),Lookup(Lookup(JSIdentifier(Object),JSString(prototype)),JSString(toString))), Declare(JSIdentifier(hasOwn)), Assign(JSIdentifier(hasOwn),Lookup(Lookup(JSIdentifier(Object),JSString(prototype)),JSString(hasOwnProperty))), Declare(JSIdentifier(push)), Assign(JSIdentifier(push),Lookup(Lookup(JSIdentifier(Array),JSString(prototype)),JSString(push))), Declare(JSIdentifier(slice)), Assign(JSIdentifier(slice),Lookup(Lookup(JSIdentifier(Array),JSString(prototype)),JSString(slice))), Declare(JSIdentifier(trim)), Assign(JSIdentifier(trim),Lookup(Lookup(JSIdentifier(String),JSString(prototype)),JSString(trim))), Declare(JSIdentifier(indexOf)), Assign(JSIdentifier(indexOf),Lookup(Lookup(JSIdentifier(Array),JSString(prototype)),JSString(indexOf))), Declare(JSIdentifier(class2type)), Assign(JSIdentifier(class2type),Call(JSFunction(None,List(),List()),List())))))))

/*
*/