package jsengine.library

import jsengine.ast.JSObject
import jsengine.ast.JSLiteralObject
import jsengine.ast.JSBoolean
import jsengine.ast.JSUndefined
import jsengine.ast.EmptyPropertySet

object BuiltinObjects {
	val _object = JSLiteralObject(List())
	val _true = JSBoolean(true)
	val _false = JSBoolean(false)
}