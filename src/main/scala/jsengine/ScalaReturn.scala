package jsengine

trait ScalaReturn;

case class ScalaReturnNumber(value: Double) extends ScalaReturn
case class ScalaReturnString(value: String) extends ScalaReturn
case object ScalaReturnUndefined extends ScalaReturn
case class ScalaReturnBoolean(bool: Boolean) extends ScalaReturn
case class ScalaReturnException(t: Throwable) extends ScalaReturn
case class ScalaReturnNotImplemented() extends ScalaReturn