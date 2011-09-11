package jsengine.runtime.tree

class RTObjectPropertyKey(val keyObject: RTObject) {
  private def getIndexValue = {
    try {
      keyObject.toNumber().toString()
    } catch {
      case _ => keyObject.toString()
    }
  }
  val key:String = getIndexValue

  override def hashCode = key.hashCode
  override def equals(that: Any):Boolean = key.equals(that)

}

object RTObjectPropertyKey {
  def apply(keyObject: RTObject) = new RTObjectPropertyKey(keyObject)
}