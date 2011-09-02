package jsengine

trait ScalaReturn;

case class ScalaReturnDouble(value: Double) extends ScalaReturn
case class ScalaReturnString(value: String) extends ScalaReturn
case object ScalaReturnUndefined extends ScalaReturn
case class ScalaReturnBoolean(bool: Boolean) extends ScalaReturn