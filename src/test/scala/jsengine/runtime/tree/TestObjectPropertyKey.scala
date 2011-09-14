package jsengine.runtime.tree

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import jsengine.runtime.library.Stdlib_String

class TestObjectPropertyKey {

	@Test def testFirstEquals() {
    val key1 = RTObjectPropertyKey(Stdlib_String("hello"))
    val key2 = RTObjectPropertyKey(Stdlib_String("hello"))
    assertThat(key1,is[Any](key2))
	}


}

