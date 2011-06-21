package jsengine.ast

trait PropertySet {
	def getProperty(name: JSString):Option[JSExpression]
	def setProperty(name: JSString, value: JSExpression):Unit
}