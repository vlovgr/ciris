package ciris.spire.readers

import ciris.ConfigReader
import spire.math._

import scala.util.{Success, Try}

trait SpireConfigReaders {
  implicit val algebraicConfigReader: ConfigReader[Algebraic] =
    ConfigReader.catchNonFatal("Algebraic")(Algebraic.apply)

  implicit val intervalRationalConfigReader: ConfigReader[Interval[Rational]] =
    ConfigReader.catchNonFatal("Interval[Rational]")(Interval.apply)

  implicit val naturalConfigReader: ConfigReader[Natural] =
    ConfigReader.fromTryOption("Natural") { value =>
      if (value.forall(_.isDigit))
        Try(Some(Natural(value)))
      else
        Success(None)
    }

  implicit val numberConfigReader: ConfigReader[Number] =
    ConfigReader.catchNonFatal("Number")(Number.apply)

  implicit val polynomialRationalConfigReader: ConfigReader[Polynomial[Rational]] =
    ConfigReader.catchNonFatal("Polynomial[Rational]")(Polynomial.apply)

  implicit val rationalConfigReader: ConfigReader[Rational] =
    ConfigReader.catchNonFatal("Rational")(Rational.apply)

  implicit val realConfigReader: ConfigReader[Real] =
    ConfigReader.catchNonFatal("Real")(Real.apply)

  implicit def safeLongConfigReader(
    implicit reader: ConfigReader[BigInt]
  ): ConfigReader[SafeLong] =
    reader.map(SafeLong.apply)

  implicit def trileanConfigReader(
    implicit reader: ConfigReader[Option[Boolean]]
  ): ConfigReader[Trilean] =
    reader.map(Trilean.apply)

  implicit def ubyteConfigReader(
    implicit reader: ConfigReader[Int]
  ): ConfigReader[UByte] =
    reader.collect("UByte") {
      case int if UByte.MinValue.toInt <= int && int <= UByte.MaxValue.toInt =>
        UByte(int)
    }

  implicit def uintConfigReader(
    implicit reader: ConfigReader[Long]
  ): ConfigReader[UInt] =
    reader.collect("UInt") {
      case long if UInt.MinValue.toLong <= long && long <= UInt.MaxValue.toLong =>
        UInt(long)
    }

  implicit def ulongConfigReader(
    implicit reader: ConfigReader[BigInt]
  ): ConfigReader[ULong] =
    reader.collect("ULong") {
      case bigInt if ULong.MinValue.toBigInt <= bigInt && bigInt <= ULong.MaxValue.toBigInt =>
        ULong.fromBigInt(bigInt)
    }

  implicit def ushortConfigReader(
    implicit reader: ConfigReader[Int]
  ): ConfigReader[UShort] =
    reader.collect("UShort") {
      case int if UShort.MinValue.toInt <= int && int <= UShort.MaxValue.toInt =>
        UShort(int)
    }
}
