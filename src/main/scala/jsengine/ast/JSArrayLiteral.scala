package jsengine.ast

case class JSArrayLiteral(elements : List[Option[JSBaseExpression]]) extends JSBaseExpression
