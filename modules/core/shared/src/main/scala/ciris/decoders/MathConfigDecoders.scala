package ciris.decoders

import ciris.ConfigDecoder
import java.math.{BigDecimal => JBigDecimal}
import java.math.{BigInteger => JBigInteger}

import scala.util.Try

trait MathConfigDecoders {
  implicit val bigIntConfigDecoder: ConfigDecoder[BigInt] =
    ConfigDecoder.fromTry("BigInt")(value => Try(BigInt(value)))

  implicit val bigDecimalConfigDecoder: ConfigDecoder[BigDecimal] =
    ConfigDecoder.fromTry("BigDecimal")(value => Try(BigDecimal(value)))

  implicit val javaBigDecimalConfigDecoder: ConfigDecoder[JBigDecimal] =
    bigDecimalConfigDecoder.map(_.underlying)

  implicit val javaBigIntegerConfigDecoder: ConfigDecoder[JBigInteger] =
    bigIntConfigDecoder.map(_.underlying)
}
