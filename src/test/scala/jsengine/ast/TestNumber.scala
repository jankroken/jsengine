package jsengine.ast

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.fail
import jsengine.library.BuiltinObjects

class TestNumber {

    @Test def testConstructEmpty() {
		val o = JSNumber(0)
    }
    
    @Test def testEquals() {
		val s1 = JSNumber(42)
		val s2 = JSNumber(42)
		assertThat(s1,is(s2))
    }

    @Test def testNotEquals() {
		val s1 = JSNumber(41)
		val s2 = JSNumber(42)
		assertThat(s1,is(not(s2)))
    }
}