package ciris.decoders

import java.math.{
  BigDecimal => JavaBigDecimal,
  BigInteger => JavaBigInteger
}

import ciris.ConfigDecoder

import scala.math.BigDecimal

trait MathConfigDecoders {
  implicit val bigIntConfigDecoder: ConfigDecoder[String, BigInt] =
    ConfigDecoder.catchNonFatal("BigInt")(BigInt(_))

  implicit val bigDecimalConfigDecoder: ConfigDecoder[String, BigDecimal] =
    ConfigDecoder.catchNonFatal("BigDecimal")(BigDecimal(_))

  implicit val javaBigDecimalConfigDecoder: ConfigDecoder[String, JavaBigDecimal] =
    ConfigDecoder.catchNonFatal("BigDecimal")(new JavaBigDecimal(_))

  implicit val javaBigIntegerConfigDecoder: ConfigDecoder[String, JavaBigInteger] =
    ConfigDecoder.catchNonFatal("BigInteger")(new JavaBigInteger(_))
}
