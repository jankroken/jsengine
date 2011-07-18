package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSRegexLiteral(value: String) extends JSObject with PropertyName