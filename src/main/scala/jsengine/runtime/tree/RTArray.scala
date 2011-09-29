package jsengine.runtime.tree

import jsengine.runtime.ExecutionContext
import jsengine.runtime.library._

class RTArray extends RTObject(Some(Stdlib_Object_Array)) {

  private var arrayContent: Map[Int,RTObject] = Map()

  override def isPrimitive = false
  override def isObject = true
  override def booleanValue = Stdlib_Boolean(true)
  override def stringValue = Stdlib_String("[ <RTArray:TODO> ]")
  override def evaluate(env: RTEnvironmentRecord) = this
  override def typeof = "object"

  override def toString():String = {
     "["+arrayContent+"]"
  }

  override def setProperty(key: RTObject, value: RTObject) = {
    key match {
      case indexNumber: Stdlib_Number if indexNumber.isDoubleValue => {
        val index = indexNumber.forcedDoubleValue.toInt
        println("array["+index+"] = "+value)
        arrayContent = arrayContent ++ Map(index -> value)
      }
      case _ => super.setProperty(key,value)
    }
  }


  override def getProperty(key: RTObject): Option[RTNamedObjectProperty] = {
    key match {
      case indexNumber: Stdlib_Number if indexNumber.isDoubleValue => {
        val index = indexNumber.forcedDoubleValue.toInt
        val optionalValue = arrayContent.get(index)
        optionalValue match {
          case Some(value) => Some(RTNamedObjectProperty(value))
          case None => Some(RTNamedObjectProperty(Stdlib_Undefined))
        }
      }
      case _ => super.getProperty(key)
    }
  }


}

object RTArray {
  def apply() = {
    new RTArray
  }
}