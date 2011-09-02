package jsengine.runtime.tree

import jsengine.runtime.library._

class RTNamedObjectProperty {
	var writeable = false
	var enumerable = false
	var configurable = false
	var get = Stdlib_Undefined
	var se = Stdlib_Undefined
}