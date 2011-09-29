package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine._

class TestSection_8_4 {

  @Test def testTypeofString() {
    val source = "typeof('hello')"
    val expected = ScalaReturnString("string")
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testStringLength() {
    val source = "'hello'.length"
    val expected = ScalaReturnDouble(5.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testEmptyStringLength() {
    val source = "''.length"
    val expected = ScalaReturnDouble(0.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testLessThan() {
    val source = "('x\\0a' < 'x\\0b')"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testGreaterThan() {
    val source = "('x\\0b' > 'x\\0a')"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testEmptyEqualsZero() {
    val source = "'' == 0"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testEmptyEqualsFalse() {
    val source = "'' == false"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
   }

  @Test def testSameConstructor() {
    val source = "var s1 = 'hello';var s2 = new String('hello'); s1.constructor == s2.constructor"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testEquals() {
    val source = "var s1 = 'hello';var s2 = new String('hello'); s1 === s2"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testNotEquals() {
    val source = "var s1 = 'hello';var s2 = new String('world'); s1 !== s2"
    val expected = ScalaReturnBoolean(true)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testConstructor() {
    val source = "'hello'.constructor"
    val expected = ScalaReturnString("String")
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

}

