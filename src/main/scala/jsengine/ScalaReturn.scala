package jsengine

import jsengine.runtime.tree.RTObject
import jsengine.runtime.library._
trait ScalaReturn;

case class ScalaReturnDouble(value: Double) extends ScalaReturn
case class ScalaReturnString(value: String) extends ScalaReturn
case object ScalaReturnUndefined extends ScalaReturn
case class ScalaReturnBoolean(bool: Boolean) extends ScalaReturn
case class ScalaReturnException(jsexception: Throwable) extends ScalaReturn
case class ScalaReturnNotImplemented()  extends ScalaReturn
case object ScalaNaN extends ScalaReturn
case object ScalaPositiveInfinity extends ScalaReturn
case object ScalaNegativeInfinity extends ScalaReturn
case class ScalaReturnObject(properties: Map[String,ScalaReturn])  extends ScalaReturn

object ScalaReturn {
  def apply(obj: RTObject):ScalaReturn = {
    obj match {
      case Stdlib_Undefined => ScalaReturnUndefined
      case bool: Stdlib_Boolean => ScalaReturnBoolean(bool.nativeBooleanValue)
      case string: Stdlib_String => ScalaReturnString(string.nativeStringValue)
      case number:Stdlib_Number => {
        number.value match {
          case NaN => ScalaNaN
          case PositiveInfinity => ScalaPositiveInfinity
          case NegativeInfinity => ScalaNegativeInfinity
          case DoubleValue(dv) => ScalaReturnDouble(dv)
        }
      }
      case obj: RTObject => {
        var properties: Map[String,ScalaReturn] = Map()
        for (((key,value)) <- obj.properties) {
          println("key:%s".format(key.keyObject))
          val scalaKey = key.keyObject match {
            case keyStr: Stdlib_String => keyStr.nativeStringValue
            case _ => key.keyObject.toString
          }
          val scalaValue = ScalaReturn(value.value)
          properties = properties ++ Map[String,ScalaReturn](scalaKey -> scalaValue)
        }
//          yield Map(key -> ScalaReturn(value))
//        }
//        val properties = obj.properties.map(((key,value)) => {(key.toString -> ScalaReturn(value))})
//        val properties : Map[String,ScalaReturn] = Map()
        ScalaReturnObject(properties)
      }
      case _ => ScalaReturnNotImplemented()
    }
  }
}