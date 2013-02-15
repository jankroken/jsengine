package jsengine.library

import jsengine.ast.JSObject
import jsengine.ast.JSBoolean
import jsengine.ast.JSUndefined

object BuiltinNativeCalls {
	
	private val nativecalls: Map[String, () => JSObject] = Map("helloworld" -> helloworld)
  
	def executeNativeCall(name: String):JSObject = nativecalls(name)()

	def helloworld():JSObject = {
	  println("hello world");
	  JSUndefined()
	}
  
}