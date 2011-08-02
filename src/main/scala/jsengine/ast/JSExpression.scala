package jsengine.ast

case class JSExpression(assignments : List[JSBaseExpression]) extends JSBaseExpression with JSStatement
trait LefthandExpression // undefined
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