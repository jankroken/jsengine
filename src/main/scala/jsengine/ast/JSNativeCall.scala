package jsengine.ast

import jsengine.library.BuiltinObjects
import jsengine.library.BuiltinNativeCalls

case class JSNativeCall(val identifier: JSString) extends JSExpression