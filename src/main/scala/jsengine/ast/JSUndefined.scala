package jsengine.ast

import jsengine.library.BuiltinObjects

class JSUndefined(prototype: PropertySet, properties: Map[JSString,JSObject]) extends JSObject(prototype, properties) {

  override def equals(other: Any):Boolean = {
    return other.isInstanceOf[JSUndefined]
  }
  
  override def hashCode:Int = {
    return 1
  }

  override def toString:String = {
    return "<undefined>"
  }
	
  override def evaluate:JSObject = {
	return this
  }
}
object JSUndefined {
	def apply():JSUndefined = {
	  return BuiltinObjects._undefined
	}
}