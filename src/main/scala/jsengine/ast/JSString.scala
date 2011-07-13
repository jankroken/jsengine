package jsengine.ast

import jsengine.library.BuiltinObjects

case class JSString(value: String) extends JSObject with PropertyName