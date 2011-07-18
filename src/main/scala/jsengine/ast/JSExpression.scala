package jsengine.ast

case class JSExpression(assignments : List[JSBaseExpression]) extends JSBaseExpression
trait LefthandExpression // undefined
case class AssignmentExpression(leftHand: LefthandExpression, righthand: JSBaseExpression) extends JSBaseExpression
case class ConditionalExpression(condition: JSBaseExpression, trueExpression: JSBaseExpression, falseExpression: JSBaseExpression)
case class LogicalOrExpression(left: JSBaseExpression, right: JSBaseExpression)
case class LogicalAndExpression(left: JSBaseExpression, right: JSBaseExpression)
case class BitwiseOrExpression(left: JSBaseExpression, right: JSBaseExpression)
case class BitwiseAndExpression(left: JSBaseExpression, right: JSBaseExpression)
case class EqualityExpression(operator: Operator, left: JSBaseExpression, right: JSBaseExpression)
case class RelaitionalExpression(operator: Operator, left: JSBaseExpression, right: JSBaseExpression)
case class ShiftExpression(operator: Operator, left: JSBaseExpression, right: JSBaseExpression)
case class AdditiveExpression(operator: Operator, left: JSBaseExpression, right: JSBaseExpression)
case class MultiplicativeExpression(operator: Operator, left: JSBaseExpression, right: JSBaseExpression)
case class UnaryExpression(operator: Operator, expression: JSBaseExpression)
case class PostfixExpression(operator: Operator, expression: JSBaseExpression)
case class NewExpression(function: JSBaseExpression, arguments: List[JSBaseExpression])
case class LookupExpression(jsobject: JSBaseExpression, key: JSBaseExpression)
case class FunctionApplicationExpression(function: JSBaseExpression, arguments: List[JSBaseExpression])
case class CallExpression
case class MemberExpression
