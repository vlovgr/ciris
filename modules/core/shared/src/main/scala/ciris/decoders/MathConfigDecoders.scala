package ciris.decoders

import java.math.{
  MathContext,
  RoundingMode,
  BigDecimal => JavaBigDecimal,
  BigInteger => JavaBigInteger
}

import ciris.ConfigDecoder

import scala.math.BigDecimal

trait MathConfigDecoders {
  implicit val bigIntConfigDecoder: ConfigDecoder[String, BigInt] =
    ConfigDecoder.catchNonFatal("BigInt")(BigInt(_))

  implicit val bigDecimalConfigDecoder: ConfigDecoder[String, BigDecimal] = {
    // Workaround loss of precision on Scala 2.10
    def exact(d: JavaBigDecimal): BigDecimal =
      new BigDecimal(
        d, {
          if (d.precision <= BigDecimal.defaultMathContext.getPrecision)
            BigDecimal.defaultMathContext
          else new MathContext(d.precision, RoundingMode.HALF_EVEN)
        }
      )

    ConfigDecoder.catchNonFatal("BigDecimal")(s => exact(new JavaBigDecimal(s)))
  }

  implicit val javaBigDecimalConfigDecoder: ConfigDecoder[String, JavaBigDecimal] =
    ConfigDecoder.catchNonFatal("BigDecimal")(new JavaBigDecimal(_))

  implicit val javaBigIntegerConfigDecoder: ConfigDecoder[String, JavaBigInteger] =
    ConfigDecoder.catchNonFatal("BigInteger")(new JavaBigInteger(_))
}
