package jsengine.runtime

import jsengine.runtime.library._
import tree.{RTReference, RTId, RTObject, RTEnvironmentRecord}

object LibraryNameAssigner {
  def assignLibraryObjects(env: RTEnvironmentRecord) {
    assignLibraryObject(env,"Object",Stdlib_Object)
    assignLibraryObject(env,"String",Stdlib_Object_String)
    assignLibraryObject(env,"Number",Stdlib_Object_Number)
  }

  def assignLibraryObject(env: RTEnvironmentRecord, name: String, obj: RTObject) {
     env.declare(RTId(name))
     env.getReference(RTId(name)) match {
       case ref: RTReference => ref.setValue(obj.evaluate(env).valueOf)
     }
  }

}