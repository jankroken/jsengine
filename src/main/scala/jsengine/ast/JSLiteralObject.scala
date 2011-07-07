package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSLiteralObject(var properties : Map[JSString,JSExpression]) extends JSObject