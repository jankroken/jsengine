package jsengine.ast

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import org.hamcrest.CoreMatchers.not

class TestBoolean {

    @Test def testConstructTrue() {
		val o = JSBoolean(true)
    }
    
    @Test def testEqualsTrue() {
		val s1 = JSBoolean(true)
		val s2 = JSBoolean(true)
		assertThat(s1,is(s2))
    }

    @Test def testEqualsFalse() {
		val s1 = JSBoolean(false)
		val s2 = JSBoolean(false)
		assertThat(s1,is(s2))
    }

    @Test def testNotEqualsTrueFalse() {
		val s1 = JSBoolean(true)
		val s2 = JSBoolean(false)
		assertThat(s1,is(not(s2)))
    }

}