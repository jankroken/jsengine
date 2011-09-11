package jsengine.runtime.tree

import jsengine.runtime.library._

class RTNamedObjectProperty(val value: RTObject) {
	var writable = false
	var enumerable = false
	var configurable = false
	var get = Stdlib_Undefined
	var set = Stdlib_Undefined
}

object RTNamedObjectProperty {
  def apply(value: RTObject) = new RTNamedObjectProperty(value)
}
