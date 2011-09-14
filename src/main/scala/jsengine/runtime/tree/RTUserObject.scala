package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class RTUserObject(prototype: RTObject) extends RTObject(Some(prototype)) {

  override def isPrimitive = false
  override def isObject = true
  override def toBoolean:Stdlib_Boolean = Stdlib_Boolean(true)
  override def evaluate(env: RTEnvironmentRecord) = this

  override def toString():String = {
     "UserObject(%s)[prototype:%s]".format(properties.toString(),prototype.toString)
  }

	/*
[[Class]] String A  String value indicating a specification defined
classification of objects.
[[Extensible]] Boolean If  true, own properties may be added to the 
object.
[[Get]] SpecOp(propertyName) ->
any
Returns the value of the named property.
[[GetOwnProperty]] SpecOp (propertyName) ->
Undefined or Property
Descriptor
Returns the Property Descriptor of the named 
own property of this object, or  undefined  if 
absent.
[[GetProperty]] SpecOp (propertyName) ->
Undefined or Property 
Descriptor
Returns the fully populated Property Descriptor 
of the named property of this object, or 
undefined if absent.
[[Put]] SpecOp (propertyName, 
any, Boolean)
Sets the specified named property to the value 
of the second parameter. The flag controls 
failure handling.
[[CanPut]] SpecOp (propertyName) ->
Boolean
Returns a Boolean value indicating whether a 
[[Put]] operation with  PropertyName can be 
performed.
[[HasProperty]] SpecOp (propertyName) ->
Boolean
Returns a Boolean value indicating whether the 
object already has a property with the given 
name.
[[Delete]] SpecOp (propertyName, 
Boolean) -> Boolean
Removes the specified named own property 
from the object. The flag controls failure 
handling.
[[DefaultValue]] SpecOp (Hint) -> primitive Hint is a  String. Returns a default value for the 
object.
[[DefineOwnProperty]] SpecOp (propertyName, 
PropertyDescriptor, 
Boolean) -> Boolean
Creates or alters the named own property to 
have the state described by a Property 
Descriptor. The flag controls failure handling


=== only for some objects

[[PrimitiveValue]] primitive Internal state information associated with this object. Of the 
standard built-in ECMAScript objects, only  Boolean, Date, 
Number, and String objects implement [[PrimitiveValue]].
[[Construct]] SpecOp(a List of 
any) -> Object
Creates an object. Invoked via the  new operator. The 
arguments to the SpecOp are the arguments passed to the 
new operator. Objects that implement this internal method 
are called constructors.
[[Call]] SpecOp(any, a List 
of any) -> any or
Reference
Executes code associated with the object. Invoked via a 
function call expression. The arguments to the SpecOp are
this object and a list containing the arguments passed to the 
function call expression. Objects that implement this internal 
method are  callable. Only  callable objects that are host 
objects may return Reference values.
[[HasInstance]] SpecOp(any) ->
Boolean
Returns a Boolean value indicating whether the argument is 
likely  an Object that  was constructed by this object. Of the 
standard built-in ECMAScript objects, only Function objects
implement [[HasInstance]].
[[Scope]] Lexical Environment A lexical environment that defines the environment in which 
a Function object is executed. Of the standard built-in 
ECMAScript objects, only  Function objects implement 
[[Scope]].
[[FormalParameters]] List of Strings A possibly empty List containing the identifier  Strings of a 
Functions  FormalParameterList. Of the standard built-in
ECMAScript objects, only  Function objects implement 
[[FormalParameterList]].
[[Code]] ECMAScript code The ECMAScript code of a function. Of the standard built-in 
ECMAScript objects, only  Function objects implement 
[[Code]].
[[TargetFunction]] Object The target function of a function object created using the 
standard built-in Function.prototype.bind method. Only 
ECMAScript objects  created using Function.prototype.bind 
have a [[TargetFunction]] internal property.
[[BoundThis]] any The pre-bound this value of a function Object created using 
the standard built-in Function.prototype.bind method. Only 
ECMAScript objects  created using Function.prototype.bind 
have a [[BoundThis]] internal property.
[[BoundArguments]] List of any The pre-bound argument values of a function Object created 
using the standard built-in Function.prototype.bind method. 
Only ECMAScript objects  created using 
Function.prototype.bind have a [[BoundArguments]] internal 
property.
[[Match]] SpecOp(String, 
index) ->
MatchResult
Tests for a regular expression match and returns a 
MatchResult value (see 15.10.2.1). Of the standard built-in 
ECMAScript objects, only RegExp objects implement 
[[Match]].
[[ParameterMap]] Object Provides a mapping between the properties of an arguments 
object (see  10.6) and the formal parameters of the 
associated function. Only ECMAScript objects that are 
arguments objects have a [[ParameterMap]] internal 
property.


	  
	 */
}

object RTUserObject {
  def apply(prototype: RTObject) = {
    new RTUserObject(prototype)
  }

}