package jsengine.ast

import jsengine.library.BuiltinObjects

trait JSSourceElement {
  def evaluate:JSObject;
}