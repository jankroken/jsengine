package jsengine.runtime.library

import jsengine.runtime.tree.RTObject
import jsengine.runtime.tree.RTEnvironmentRecord

class Stdlib_Number(value: Double) extends RTObject {
	def evaluate(env: RTEnvironmentRecord):RTObject = { this }
	def nativeDoubleValue = value
}

object Stdlib_Number {
    def apply(value: String) = {
      new Stdlib_Number(value.toDouble)
    }
}