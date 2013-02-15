package jsengine.ast

object EmptyPropertySet extends PropertySet {
	def getProperty(name: JSString) : Option[JSBaseExpression] = None
	def setProperty(name: JSString, value: JSBaseExpression) : Unit = {
	  throw new RuntimeException("Invalid Operation: not allowed to update")
	}
}