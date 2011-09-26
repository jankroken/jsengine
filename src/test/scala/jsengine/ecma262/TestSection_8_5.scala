package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine.runtime.library.{NegativeInfinity, PositiveInfinity}
import jsengine.{ScalaNegativeInfinity, ScalaPositiveInfinity, ScalaNaN, JSRunner}

class TestSection_8_5 {

  @Test def testSimpleNaN() {
      val source = "NaN"
      val expected = ScalaNaN
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testSimpleInfinity() {
      val source = "Infinity"
      val expected = ScalaPositiveInfinity
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNegativeInfinity() {
      val source = "-Infinity"
      val expected = ScalaNegativeInfinity
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

