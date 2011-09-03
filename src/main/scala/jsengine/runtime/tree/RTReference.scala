package jsengine.runtime.tree

import jsengine.runtime.library._

class RTReference {
	var base = Stdlib_Undefined
	var referenced_name = Stdlib_Undefined
	var strict_reference: Boolean = false
}