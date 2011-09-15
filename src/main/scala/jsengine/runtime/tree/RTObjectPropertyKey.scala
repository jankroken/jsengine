package jsengine.runtime.tree

class RTObjectPropertyKey(val keyObject: RTObject) {
  private def getIndexValue = {
    try {
      val numberString = keyObject.toNumber().toString()
      if(numberString == "NaN") {
        keyObject.toString
      } else {
        numberString
      }
    } catch {
      case _ => keyObject.toString()
    }
  }
  val key:String = getIndexValue

  override def hashCode = key.hashCode
  override def equals(that: Any):Boolean = {
      println("RTObjectPropertyKey.equals(%s,%s)".format(this,that))
      that match {
        case otherKey:RTObjectPropertyKey => {
          println("RTObjectPropertyKey.equals:2(%s,%s)".format(key,otherKey.key))
          key.equals(otherKey.key)
        }
        case _ => false
      }
  }
  override def toString = "okey("+keyObject.toString+")"

}

object RTObjectPropertyKey {
  def apply(keyObject: RTObject) = {
      new RTObjectPropertyKey(keyObject)
  }
}