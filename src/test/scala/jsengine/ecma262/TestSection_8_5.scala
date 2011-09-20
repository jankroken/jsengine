package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine.{ScalaNaN, JSRunner}

class TestSection_8_5 {

  @Test def testSimpleNaN() {
      val source = "NaN"
      val expected = ScalaNaN
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

