package ciris.readers

import ciris.ConfigReader
import java.math.{BigDecimal => JBigDecimal}
import java.math.{BigInteger => JBigInteger}

import scala.util.Try

trait MathConfigReaders {
  implicit val bigIntConfigReader: ConfigReader[BigInt] =
    ConfigReader.fromTry("BigInt")(value => Try(BigInt(value)))

  implicit val bigDecimalConfigReader: ConfigReader[BigDecimal] =
    ConfigReader.fromTry("BigDecimal")(value => Try(BigDecimal(value)))

  implicit val jBigDecimalConfigReader: ConfigReader[JBigDecimal] = {
    bigDecimalConfigReader.map(_.underlying())
  }

  implicit val jBigIntegerConfigReader: ConfigReader[JBigInteger] = {
    bigIntConfigReader.map(_.underlying())
  }

}
