package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is

class JSRunnerTestBasics {

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
      val expected = ScalaReturnDouble(1.0)
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

  @Test def testSimpleFunctionNoReturn() {
      val source = "function foo() { 1 }; foo()"
      val expected = ScalaReturnUndefined
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
      val expected = new ScalaReturnDouble(5.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testFunctionScope() {
      val source = "var a = 3; function foo(x) { var c = 5; return a+x+c }; foo(1)"
      val expected = new ScalaReturnDouble(9.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testRecursion() {
      val source = "function fib(x) { if(x>1) { return x*fib(x-1) } else { return 1 } }; fib(5)"
      val expected = new ScalaReturnDouble(120.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testMultipleCalls() {
      val source =
        "function a() { return 2 };" +
        "function b() { return a()*3 };" +
        "b()"
      val expected = new ScalaReturnDouble(6.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testClosure() {
      val source =
        "function counter() {"+
        "   var x = 0; "+
        "   function accessor() { "+
        "      x = x + 1; "+
        "      return x "+
        "   }; "+
        "   return accessor "+
        "}; "+
        "var count = counter();"+
        "count() + count()"
      val expected = ScalaReturnDouble(3.0)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

