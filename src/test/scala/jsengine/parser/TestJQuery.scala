package jsengine.parser

import org.junit.{Ignore, Test}

import jsengine.rewriter._
import scala.io.Source

class TestJQuery {

  def testFile(filename: String) {
    val result = JSParser.parse(JSParser.source, Source.fromURL(this.getClass().getClassLoader().getResource(filename)).reader)
    println(result)
    result match {
      case JSParser.Success(ast, _) => println(JSCallRewriter.rewriteSource(ast))
      case JSParser.NoSuccess(message, src) => println("failed")
    }
  }

  @Ignore
  @Test def testPart1 {
    testFile("jquery_part1.js")
  }

  @Ignore
  @Test def testPart2 {
    testFile("jquery_part2.js")
  }

  @Ignore
  @Test def testPart3 {
    testFile("jquery_part3.js")
  }

  @Ignore
  @Test def testPart4 {
    testFile("jquery_part4.js")
  }

  @Ignore
  @Test def testPart5 {
    testFile("jquery_part5.js")
  }

  @Ignore
  @Test def testPart6 {
    testFile("jquery_part6.js")
  }

  @Ignore
  @Test def testComplete {
    testFile("jquery-1.6.2.js")
  }
}