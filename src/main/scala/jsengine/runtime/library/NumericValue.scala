package jsengine.runtime.library

sealed trait NumericValue
case class DoubleValue(value: Double) extends NumericValue
case object NaN extends NumericValue
case object PositiveInfinity extends NumericValue
case object NegativeInfinity extends NumericValue
