package jsengine.ast

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.fail
import jsengine.library.BuiltinObjects

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