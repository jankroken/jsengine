package jsengine.runtime.tree

case class RTTypeError(msg: String, cause: Throwable = null) extends RuntimeException(msg,cause)