package jsengine.library

import jsengine.ast.JSObject
import jsengine.ast.JSBoolean
import jsengine.ast.JSUndefined
import jsengine.ast.EmptyPropertySet

object BuiltinObjects {
	val _object = new JSObject(EmptyPropertySet,Map())
	val _true = new JSBoolean(_object,Map(),true)
	val _false = new JSBoolean(_object,Map(),false)
	val _undefined = new JSUndefined(_object,Map())
}