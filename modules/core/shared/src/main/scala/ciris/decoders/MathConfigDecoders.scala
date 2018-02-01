package ciris.decoders

import ciris.ConfigDecoder
import java.math.{BigDecimal => JBigDecimal}
import java.math.{BigInteger => JBigInteger}

import scala.util.Try

private[ciris] trait MathConfigDecoders {
  implicit val bigIntConfigDecoder: ConfigDecoder[String, BigInt] =
    ConfigDecoder.fromTry("BigInt")(value => Try(BigInt(value)))

  implicit val bigDecimalConfigDecoder: ConfigDecoder[String, BigDecimal] =
    ConfigDecoder.fromTry("BigDecimal")(value => Try(BigDecimal(value)))

  implicit val javaBigDecimalConfigDecoder: ConfigDecoder[String, JBigDecimal] =
    bigDecimalConfigDecoder.map(_.underlying)

  implicit val javaBigIntegerConfigDecoder: ConfigDecoder[String, JBigInteger] =
    bigIntConfigDecoder.map(_.underlying)
}
