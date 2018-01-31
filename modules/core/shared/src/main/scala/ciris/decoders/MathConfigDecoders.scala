package ciris.decoders

import ciris.ConfigDecoder
import java.math.{BigDecimal => JavaBigDecimal}
import java.math.{BigInteger => JavaBigInteger}

import scala.util.Try

trait MathConfigDecoders {
  implicit val bigIntConfigDecoder: ConfigDecoder[String, BigInt] =
    ConfigDecoder.fromTry("BigInt")(value => Try(BigInt(value)))

  implicit val bigDecimalConfigDecoder: ConfigDecoder[String, BigDecimal] =
    ConfigDecoder.fromTry("BigDecimal")(value => Try(BigDecimal(value)))

  implicit val javaBigDecimalConfigDecoder: ConfigDecoder[String, JavaBigDecimal] =
    bigDecimalConfigDecoder.map(_.underlying)

  implicit val javaBigIntegerConfigDecoder: ConfigDecoder[String, JavaBigInteger] =
    bigIntConfigDecoder.map(_.underlying)
}
