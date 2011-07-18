package jsengine.ast

trait PropertySet {
	def getProperty(name: JSString):Option[JSBaseExpression]
	def setProperty(name: JSString, value: JSBaseExpression):Unit
}