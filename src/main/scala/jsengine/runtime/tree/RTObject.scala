package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext

abstract class RTObject extends RTExpression {
	var properties: List[RTNamedObjectProperty] = List()
}