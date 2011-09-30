package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine.runtime.library.{NegativeInfinity, PositiveInfinity}
import jsengine._

class TestSection_8_5 {

  @Test def testSimpleNaN() {
    val source = "NaN"
    val expected = ScalaNaN
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNaNEqualsNaN() {
    val source = "NaN == NaN"
    val expected = ScalaReturnBoolean(false)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testTypeofNaN() {
    val source = "typeof(NaN)"
    val expected = ScalaReturnString("number")
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNaNNotLessOrEqual0() {
    val source = "NaN <= 0"
    val expected = ScalaReturnBoolean(false)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNaNNotGreaterOrEqual0() {
    val source = "NaN >= 0"
    val expected = ScalaReturnBoolean(false)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNaNNotGreaterThanOrEqualOrLessAndEqualThan0() {
    val source = "(NaN <= 0) || (NaN >= 0)"
    val expected = ScalaReturnBoolean(false)
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

  @Test def testTypeofNegativeInfinity() {
    val source = "typeof(-Infinity)"
    val expected = ScalaReturnString("number")
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testInfinityCompare() {
    val source = "Infinity == +Infinity"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNaNPropertyNotAssigned() {
    val source = "Number.NaN = 1; Number.NaN === 1"
    val expected = ScalaReturnBoolean(false)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testDivisionByPositiveZero() {
    val source = "1.0 / +0"
    val expected = ScalaPositiveInfinity
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testDivisionByNegativeZero() {
    val source = "1.0 / -0"
    val expected = ScalaNegativeInfinity
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testPositiveInfinityConstant() {
    val source = "Number.POSITIVE_INFINITY == Infinity"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNegativeInfinityConstant() {
    val source = "Number.NEGATIVE_INFINITY == -Infinity"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

}

