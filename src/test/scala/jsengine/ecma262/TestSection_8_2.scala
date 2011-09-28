package jsengine.ecma262

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine._

class TestSection_8_2 {

  @Test def testNull() {
      val source = "null"
      val expected = ScalaReturnNull
      val retval = new JSRunner().run(source)
      assertThat(retval,is[Any](expected))
  }

}

