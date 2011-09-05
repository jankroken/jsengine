package jsengine.runtime.tree

case class RTReferenceError(msg: String, cause: Throwable = null) extends RuntimeException(msg,cause)