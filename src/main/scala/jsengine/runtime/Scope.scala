package jsengine.runtime

import jsengine.ast.JSObject

sealed class Scope(val currentScope: JSObject, parent: Option[Scope]) {
	
}