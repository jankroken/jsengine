package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is


class JSRunnerTestArrays {

	@Test def testBlockNotObject() {
		val source = "var a = [4,8,15,16,23,42]; a[0]"
    val expected = ScalaReturnDouble(4.0)
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}

}

