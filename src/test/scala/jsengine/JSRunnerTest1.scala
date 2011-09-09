package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSSource
import jsengine.parser.JSParser
import jsengine.runtime.library._


class JSRunnerTest1 {
	@Test def testUndefined() {
		val source = "undefined"
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](ScalaReturnUndefined))
	}

	@Test def testTwoUndefined() {
		val source = "undefined;undefined"
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](ScalaReturnUndefined))
	}
	
	@Test def testVariableAssignmentAndLookup() {
	    val source = "var x = true; x"
	    val expected = ScalaReturnBoolean(true)
	    val retval = new JSRunner().run(source)
	    assertThat(retval,is[Any](expected))
	}
	
	@Test def testInteger() {
		val source = "1.0"
		val expected = ScalaReturnNumber(1.0)
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}
	
	@Test def testString() {
		val source = "'hello world'"
		val expected = ScalaReturnString("hello world")
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}
	
	@Test def testBooleanAnd() {
		val source = "true && false"
		val expected = ScalaReturnBoolean(false)
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}
	
	@Test def testSimpleFunction() {
		val source = "function foo() { return true } ; foo()";
		val expected = ScalaReturnBoolean(true)
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}
	
	@Test def testFunctionArgument() {
	    val source = "function foo(x) { return x } ; foo(true)"
	    val expected = ScalaReturnBoolean(true)
	    val retval = new JSRunner().run(source)
	    assertThat(retval,is[Any](expected))
	}

  @Test def testSimpleAddition() {
      val source = "2+3"
      val expected = new ScalaReturnNumber(5.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testFunctionScope() {
      val source = "var a = 3; function foo(x) { var c = 5; return a+x+c }; foo(1)"
      val expected = new ScalaReturnNumber(9.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

