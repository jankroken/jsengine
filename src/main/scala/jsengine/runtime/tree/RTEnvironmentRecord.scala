package jsengine.runtime.tree

class RTEnvironmentRecord(val parent:Option[RTEnvironmentRecord]) {

  var references: Map[RTId,RTReference] = Map()
  
  def declare(id: RTId) {
    val storedReference = references.get(id)
    storedReference match {
      case None => true
        references = references ++ Map(id -> new RTReference(id))
      case Some(ref) => true
    }
  }
  
  def getReference(id: RTId): RTReferenceType = {
    val storedReference = references.get(id)
    storedReference match {
      case None =>
        parent match {
          case None => RTNoReference(id)
          case Some(parent) => parent.getReference(id)
        }
      case Some(ref) => ref
    }
  }
}