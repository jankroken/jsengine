package jsengine.ast

object EmptyPropertySet extends PropertySet{
	def getProperty(name: JSString) : Option[JSExpression] = { return None }
	def setProperty(name: JSString, value: JSExpression) : Unit = {
	  throw new RuntimeException("Invalid Operation: not allowed to update")
	}
}