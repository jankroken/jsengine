package jsengine.library

import jsengine.ast.JSObject
import jsengine.ast.JSBoolean
import jsengine.ast.JSUndefined
import jsengine.ast.EmptyPropertySet

object BuiltinNativeCalls {
	
	private val nativecalls: Map[String, () => JSObject] = Map("helloworld" -> helloworld)
  
	def executeNativeCall(name: String):JSObject = {
	  	return nativecalls(name)()
	}
  
  
	def helloworld():JSObject = {
	  println("hello world");
	  return BuiltinObjects._undefined
	}
  
}