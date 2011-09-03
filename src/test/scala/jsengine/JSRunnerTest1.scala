package jsengine

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString
import jsengine.ast.JSSource
import jsengine.parser.JSParser

class JSRunnerTest1 {
	@Test def testUndefined() {
		val source = "undefined"
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](ScalaReturnUndefined))
	}

	@Test def testTwoUndefined() {
		val source = "undefined;undefined"
		val retval = new JSRunner().run(source)
		assertThat(retval,is[Any](ScalaReturnUndefined))
	}
	
	@Test def testVariableAssignmentAndLookup() {
	    val source = "var x = true; x"
	    val expected = ScalaReturnBoolean(true)
	    val retval = new JSRunner().run(source)
	    assertThat(retval,is[Any](expected))
	}

}

