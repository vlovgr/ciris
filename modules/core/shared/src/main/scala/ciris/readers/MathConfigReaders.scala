package ciris.readers

import ciris.ConfigReader

import scala.util.Try

trait MathConfigReaders {
  implicit val bigIntConfigReader: ConfigReader[BigInt] =
    ConfigReader.fromTry("BigInt")(value => Try(BigInt(value)))

  implicit val bigDecimalConfigReader: ConfigReader[BigDecimal] =
    ConfigReader.fromTry("BigDecimal")(value => Try(BigDecimal(value)))
}
