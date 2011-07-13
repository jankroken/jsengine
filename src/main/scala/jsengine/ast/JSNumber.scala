package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSNumber(val value: String) extends JSObject with PropertyName 