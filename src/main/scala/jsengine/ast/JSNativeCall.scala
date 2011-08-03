package jsengine.ast

import jsengine.library.BuiltinObjects
import jsengine.library.BuiltinNativeCalls

case class JSNativeCall(val identifier: JSIdentifier) extends JSBaseExpression