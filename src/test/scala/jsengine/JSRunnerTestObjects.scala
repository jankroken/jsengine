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


class JSRunnerTestObjects {

	@Test def testBlockNotObject() {
		val source = "{}"
    val expected = ScalaReturnUndefined
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](expected))
	}

  @Test def testEmptyObject() {
    val source = "var a = {};a"
    val expected = ScalaReturnObject()
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

  @Test def testSimpleObject() {
    val source = "var a = { hello : 'world' };a"
    val expected = ScalaReturnObject()
    val retval = new JSRunner().run(source)
    assertThat(retval,is[Any](expected))
  }

}

