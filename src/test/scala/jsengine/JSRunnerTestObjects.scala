package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is


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

  @Test def nonexistentmember() {
    val source = "var foo = {};foo.bar"
    val expected = ScalaReturnUndefined
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testThis() {
    val source = "var o = { a: 1, f: function() { return this.a } }; o.f()"
    val expected = ScalaReturnDouble(1.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testConstructorFunction() {
    val source = "function Construct(value) {" +
                 "  this.v = value "+
                 "}; "+
                 "var o = new Construct(10); "+
                 "o.v"
    val expected = ScalaReturnDouble(10)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

}

