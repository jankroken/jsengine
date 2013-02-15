package jsengine.runtime

import jsengine.ast._
import jsengine.runtime.types._

sealed class Scope(parent: Option[Scope]) {
	var values:Map[JSIdentifier,Reference] = Map()
	
	def declare(id:JSIdentifier) {
	  	values = values + (id -> new Reference)
	}
}

object Scope {
	def apply = new Scope(None)
	def apply(outerScope:Scope) = new Scope(Some(outerScope))
}