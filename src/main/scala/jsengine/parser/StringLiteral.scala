package jsengine.parser

import jsengine.library.BuiltinObjects

class StringLiteral(val rawQuotedString: String) {
	
	def getUnqotedString(quotechar: Char): String = {
		val rawSeq: Seq[Char] = rawQuotedString
		def unquoteAndRemoveTrailingQuote(s: Seq[Char]):String = {
			s match {
			  case Seq(quotechar) => return ""
			  case Seq('\\','u',d1,d2,d3,d4,rest @ _*) => return "[UNICODE]"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','n',rest @ _*) => return "\n"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','"',rest @ _*) => return "\""+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','\'',rest @ _*) => return "\'"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','b',rest @ _*) => return "\b"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','f',rest @ _*) => return "\f"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','t',rest @ _*) => return "\t"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\','v',rest @ _*) => return "\0x0b"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq('\\',c,rest @ _*) => return "\\c"+unquoteAndRemoveTrailingQuote(rest)
			  case Seq(c, rest @ _*) => return ""+c+unquoteAndRemoveTrailingQuote(rest)
			  case _ => throw new RuntimeException("String is not qouted: "+rawQuotedString)
			}
		}
	  
		rawSeq match {
		  	case Seq(quotechar, rest @ _*) => unquoteAndRemoveTrailingQuote(rest)
		  	case _ => throw new RuntimeException("String is not qouted: "+rawQuotedString)
		}
		
	}
}
object StringLiteral {
	def apply(s: String):StringLiteral = {
		return new StringLiteral(s)
	}
}