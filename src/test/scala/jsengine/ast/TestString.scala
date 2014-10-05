package jsengine.ast

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is

class TestString {

    @Test def testConstructEmpty() {
		val o = JSString("")
    }
    
    @Test def testEquals() {
		val s1 = JSString("string1")
		val s2 = JSString("string1")
		assertThat(s1,is(s2))
    }

}