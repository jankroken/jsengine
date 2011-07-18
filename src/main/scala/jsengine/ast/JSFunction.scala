package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSFunction(val functionName: Option[JSString], val arguments: List[JSString], source: List[JSSourceElement]) extends JSBaseExpression
