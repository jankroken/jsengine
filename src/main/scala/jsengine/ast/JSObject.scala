package jsengine.ast

import jsengine.library.BuiltinObjects

class JSObject(val prototype : PropertySet,
    		   var properties : Map[JSString,JSExpression]) extends PropertySet with JSExpression {
	
	def getProperty(name: JSString): Option[JSExpression] = {
	  val value = properties.get(name);
	  value match {
	    case Some(ref) => value 
	    case None => prototype.getProperty(name)
	  }
	  if (properties.get(name) != None) {
	    return properties.get(name);
	  } else {
	    return prototype.getProperty(name);
	  }
	}
	
	def setProperty(name: JSString, value:JSExpression):Unit = {
	  properties += (name -> value)
	}

	override def toString():String = {
		return "AST:Object="+properties
	}
	
	def create:JSObject = {
		return new JSObject(this,Map())
	}
	
}
object JSObject {
  def apply:JSObject = {
    return new JSObject(BuiltinObjects._object,Map())
  }
}