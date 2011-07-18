package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSLiteralObject(var properties : List[(PropertyName,JSBaseExpression)]) extends JSObject