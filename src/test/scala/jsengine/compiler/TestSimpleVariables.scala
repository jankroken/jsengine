package jsengine.compiler

import org.junit.Test
import org.junit.Assert.fail

import jsengine.parser.JSParser

class TestSimpleVariables {

	@Test def testTwoFunctions() {
		val function = """
			function helloworld () { 
				@NATIVECALL(helloworld);
				var x = 2, y;
				var f = function inner () {
					@NATIVECALL(helloworld)
				}
			}
		"""
		val result = JSParser.parse(JSParser.source,function)
		println("result="+result)
		result match {
		  case JSParser.Success(source,_) => println("SUCCESS source="+source)
		  case JSParser.Failure(message,_) => fail(message)
		  case x => fail("wtf? x="+x)
	
		}
	}
 }