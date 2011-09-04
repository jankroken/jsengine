package jsengine.runtime.tree

class CallObject(val env: RTEnvironmentRecord,val args: List[RTObject]) {
	
}
object CallObject {
	def apply(env: RTEnvironmentRecord,args: List[RTObject]) = {
		new CallObject(env,args)
	}
}