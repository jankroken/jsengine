package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.junit.matchers.JUnitMatchers.hasItems
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

import jsengine.ast.JSString

class TestStringLiteral {

    @Test def testEmptyQuotedString {
    	val result = StringLiteral("\"\"").getUnqotedString
    	assertThat(result,is[Any](""))
    }

    @Test def testSimple {
    	val result = StringLiteral("\"hello\"").getUnqotedString
    	assertThat(result,is[Any]("hello"))
    }

    @Test def testStringWithNewline {
    	val result = StringLiteral("\"hello\\nworld\"").getUnqotedString
    	assertThat(result,is[Any]("hello\nworld"))
    }
    
//    @Test { val expected = classOf[RuntimeException] }
    @Test
    def testEmptyUnquotedString { 
    	val result = StringLiteral("").getUnqotedString
    	println("result="+result)
    }

    
}