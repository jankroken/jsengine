package jsengine.ast

trait JSBaseExpression extends JSStatement
case class JSExpression(assignments : List[JSBaseExpression]) extends JSBaseExpression with JSStatement
case class AssignmentExpression(operator: Operator, leftHand: JSBaseExpression, righthand: JSBaseExpression) extends JSBaseExpression
case class ConditionalExpression(condition: JSBaseExpression, trueExpression: JSBaseExpression, falseExpression: JSBaseExpression) extends JSBaseExpression
case class BinaryExpression(right: JSBaseExpression, extensions: List[BinaryExtension]) extends JSBaseExpression
case class BinaryExtension(operator: Operator, right: JSBaseExpression)
case class UnaryExpression(operators: List[Operator], expression: JSBaseExpression) extends JSBaseExpression
case class PostfixExpression(expression: JSBaseExpression, operator: Operator) extends JSBaseExpression
case class CallExpression(news: Int, function: JSBaseExpression, applicationExtendsions: List[ApplicationExtension]) extends JSBaseExpression
trait ApplicationExtension
case class ApplyArguments(arglist: List[JSBaseExpression]) extends ApplicationExtension
case class ApplyLookup(expr: JSBaseExpression) extends ApplicationExtension
case class JSArrayLiteral(elements : List[Option[JSBaseExpression]]) extends JSBaseExpression
case class JSFunction(val functionName: Option[JSIdentifier], val arguments: List[JSIdentifier], source: List[JSStatement]) extends JSBaseExpression
case class JSBoolean(value: Boolean) extends JSObject
case class JSIdentifier(value: String) extends JSObject with PropertyName
case class JSNativeCall(val identifier: JSIdentifier) extends JSBaseExpression
case class Operator(value: String)
case class JSLiteralObject(var properties : List[(PropertyName,JSBaseExpression)]) extends JSObject
case class JSNumber(val value: String) extends JSObject with PropertyName
trait JSObject extends JSBaseExpression
case class JSString(value: String) extends JSObject with PropertyName
case class JSRegexLiteral(value: String) extends JSObject with PropertyName
trait PropertyName extends JSBaseExpression

// after rewrite

case class Assign(left: JSBaseExpression, value: JSBaseExpression) extends JSBaseExpression
case class OperatorCall(operator: Operator, arguments: List[JSBaseExpression]) extends JSBaseExpression
case class Lookup(expr: JSBaseExpression, index: JSBaseExpression) extends JSBaseExpression
case class New(function: JSBaseExpression, arguments: List[JSBaseExpression]) extends JSBaseExpression
case class Call(function: JSBaseExpression, arguments: List[JSBaseExpression]) extends JSBaseExpression
case class BuiltIn(name: String) extends JSBaseExpression
case class JSFunctionExpression(name: Option[JSIdentifier],variables: List[JSIdentifier],args: List[JSIdentifier], source: List[JSStatement]) extends JSBaseExpression
