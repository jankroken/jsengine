package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine._

class TestSection_8_3 {

  @Test def testTypeofTrue() {
      val source = "typeof(true)"
      val expected = ScalaReturnString("boolean")
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testTypeofFalse() {
      val source = "typeof(false)"
    val expected = ScalaReturnString("boolean")
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ2TrueTrue() {
      val source = "true == true"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ2TrueFalse() {
      val source = "true == false"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ2FalseTrue() {
      val source = "false == true"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ3TrueTrue() {
      val source = "true === true"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ3TrueFalse() {
      val source = "true === false"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testEQ3FalseTrue() {
      val source = "false === true"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ3TrueTrue() {
      val source = "true !== true"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ3TrueFalse() {
      val source = "true !== false"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ3FalseTrue() {
      val source = "false !== true"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ2TrueTrue() {
      val source = "true != true"
      val expected = ScalaReturnBoolean(false)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ2TrueFalse() {
      val source = "true != false"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

  @Test def testNotEQ2FalseTrue() {
      val source = "false != true"
      val expected = ScalaReturnBoolean(true)
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

