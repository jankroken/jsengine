package jsengine.parser

import org.junit.Test

import ParserTestSupport.verifyNumericLiteral
import jsengine.ast.JSNumber

class TestJSParserNumbericLiterals {

  
    @Test def testZero() {
    	val source = "0"
    	val ast = JSNumber("0")
    	verifyNumericLiteral(source,ast)
    }
  
    @Test def testSimpleInteger() {
    	val source = "1234"
    	val ast = JSNumber("1234")
    	verifyNumericLiteral(source,ast)
    }

    @Test def testSimpleIntegerWithExponent() {
    	val source = "1234E12"
    	val ast = JSNumber("1234E12")
    	verifyNumericLiteral(source,ast)
    }

    @Test def testSimpleDecimal() {
    	val source = "1234.56"
    	val ast = JSNumber("1234.56")
    	verifyNumericLiteral(source,ast)
    }

    @Test def testSimpleDecimalWithNegativeExponent() {
    	val source = "1234.56e-4"
    	val ast = JSNumber("1234.56e-4")
    	verifyNumericLiteral(source,ast)
    }

    @Test def testZeroDotDecimal() {
    	val source = "0.56"
    	val ast = JSNumber("0.56")
    	verifyNumericLiteral(source,ast)
    }

    @Test def testSmallDecimal() {
    	val source = ".56"
    	val ast = JSNumber(".56")
    	verifyNumericLiteral(source,ast)
    }
    
    @Test def testHexNumber() {
    	val source = "0x0d0a"
    	val ast = JSNumber("0x0d0a")
    	verifyNumericLiteral(source,ast)
    }
}