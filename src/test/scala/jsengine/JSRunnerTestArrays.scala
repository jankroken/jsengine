package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is


class JSRunnerTestArrays {

	@Test def testSimpleArray() {
		val source = "var a = [4,8,15,16,23,42]; a[0]"
    val expected = ScalaReturnDouble(4.0)
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}

  @Test def testInsertingIntoEmptyArray() {
    val source = "var a = []; a[0] = 42; a[0]"
    val expected = ScalaReturnDouble(42.0)
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testInsertingWithInterval() {
    val source = "var a = [0]; a[4] = 4; a[2]"
    val expected = ScalaReturnUndefined
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }
}

