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
import jsengine.runtime.tree.RTUserObject


class JSRunnerTestObjects {

	@Test def testBlockNotObject() {
		val source = "{}"
    val expected = ScalaReturnUndefined
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}

  @Test def testEmptyObject() {
    val source = "var a = {};a"
    val expected = ScalaReturnObject(Map())
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testSimpleObject() {
    val source = "var a = { hello : 'world' };a"
    val expected = ScalaReturnObject(Map("hello" -> ScalaReturnString("world")))
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testLookupObjectValue() {
    val source = "var a = { b : 2 };a.b"
    val expected = ScalaReturnDouble(2.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testObjectFunction() {
    val source = "var a = { f: function () { return 3 } };a.f()"
    val expected = ScalaReturnDouble(3.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testThis() {
    val source = "var o = { a: 1, f: function() { return this.a } }; o.f()"
    val expected = ScalaReturnDouble(1.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

}

