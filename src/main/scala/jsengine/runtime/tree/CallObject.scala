package jsengine.runtime.tree

class CallObject(val env: RTEnvironmentRecord,val args: List[RTObject],val thisRef: Option[RTObject]) {
	
}

object CallObject {
	def apply(env: RTEnvironmentRecord,args: List[RTObject]) = {
		new CallObject(env,args,None)
	}

  def apply(env: RTEnvironmentRecord,args: List[RTObject],thisRef: RTObject) = {
    new CallObject(env,args,Some(thisRef))
  }
}