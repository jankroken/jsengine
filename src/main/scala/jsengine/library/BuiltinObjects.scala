package jsengine.library

import jsengine.ast.JSLiteralObject
import jsengine.ast.JSBoolean

object BuiltinObjects {
	val _object = JSLiteralObject(List())
	val _true = JSBoolean(true)
	val _false = JSBoolean(false)
}