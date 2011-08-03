package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSFunction(val functionName: Option[JSIdentifier], val arguments: List[JSIdentifier], source: List[JSSourceElement]) extends JSBaseExpression
