package jsengine.parser

import org.junit.Test
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.is
import org.junit.Assert.fail

class TestStringLiteral {

    @Test def testEmptyQuotedString() {
    	val result = StringLiteral("\"\"").getUnqotedString('"')
    	assertThat(result,is[Any](""))
    }

    @Test def testSimple() {
    	val result = StringLiteral("\"hello\"").getUnqotedString('"')
    	assertThat(result,is[Any]("hello"))
    }

    @Test def testStringWithNewline() {
    	val result = StringLiteral("\"hello\\nworld\"").getUnqotedString('"')
    	assertThat(result,is[Any]("hello\nworld"))
    }
    
//    @Test{val expected = classOf[RuntimeException] }
    @Test
    def testEmptyUnquotedString() {
    	try {
	    	val result = StringLiteral("").getUnqotedString('"')
	    	println("result="+result)
	    	fail("the string is not quoted")
    	} catch {
    	  case e:RuntimeException => println("succeeded")  
    	  case _ => fail("wrong exception")
    	}
	}

    @Test def testStringWithColonAndSpace() {
    	val result = StringLiteral("'Date: '").getUnqotedString('\'')
    	assertThat(result,is[Any]("Date: "))
    }

    @Test def testSlash() {
    	val result = StringLiteral("'/'").getUnqotedString('\'')
    	assertThat(result,is[Any]("/"))
    }
    
//    'Date: ', today.getMonth()+1, '/', today.getDate(), '/', today.getYear())

    
}