package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine._

class TestSection_8_1 {

  @Test def testTypeofUndefined() {
      val source = "typeof(undefined)"
      val expected = ScalaReturnString("undefined")
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testTypeofVoidZero() {
      val source = "typeof(void 0)"
      val expected = ScalaReturnString("undefined")
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testUndefinedEqualsVoidZero() {
      val source = "undefined === void 0"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testUninitializedVariable() {
      val source = "var x;x"
      val expected = ScalaReturnUndefined
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testTypeofUninitializedVariable() {
      val source = "var x; typeof(x)"
      val expected = ScalaReturnString("undefined")
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testUnspecifiedArgument() {
      val source = "function f(x) { return x }; f()"
      val expected = ScalaReturnUndefined
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testUnknownProperty() {
      val source = "new Object().noExist"
      val expected = ScalaReturnUndefined
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testTypeofArgument() {
      val source = "function f(x) { return typeof(x) }; f(1) === 'undefined'"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

