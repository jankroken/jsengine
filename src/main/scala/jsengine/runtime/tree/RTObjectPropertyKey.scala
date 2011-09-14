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
  override def equals(that: Any):Boolean = {
      that match {
        case otherKey:RTObjectPropertyKey => key.equals(otherKey.key)
        case _ => false
      }
  }
  override def toString = "okey("+keyObject.toString+")"
//  override def toString = "okey"

}

object RTObjectPropertyKey {
  def apply(keyObject: RTObject) = {
      println("creating a new propertyKey with value: %s".format(keyObject))
      new RTObjectPropertyKey(keyObject)
  }
}