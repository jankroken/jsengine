package jsengine

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
case class ScalaReturnObject() extends ScalaReturn