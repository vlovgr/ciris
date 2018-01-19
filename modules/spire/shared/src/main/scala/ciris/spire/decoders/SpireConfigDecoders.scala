package ciris.spire.decoders

import ciris.ConfigDecoder
import spire.math._

import scala.util.{Success, Try}

trait SpireConfigDecoders {
  implicit val algebraicConfigDecoder: ConfigDecoder[Algebraic] =
    ConfigDecoder.catchNonFatal("Algebraic")(Algebraic.apply)

  implicit val intervalRationalConfigDecoder: ConfigDecoder[Interval[Rational]] =
    ConfigDecoder.catchNonFatal("Interval[Rational]")(Interval.apply)

  implicit val naturalConfigDecoder: ConfigDecoder[Natural] =
    ConfigDecoder.fromTryOption("Natural") { value =>
      if (value.forall(_.isDigit))
        Try(Some(Natural(value)))
      else
        Success(None)
    }

  implicit val numberConfigDecoder: ConfigDecoder[Number] =
    ConfigDecoder.catchNonFatal("Number")(Number.apply)

  implicit val polynomialRationalConfigDecoder: ConfigDecoder[Polynomial[Rational]] =
    ConfigDecoder.catchNonFatal("Polynomial[Rational]")(Polynomial.apply)

  implicit val rationalConfigDecoder: ConfigDecoder[Rational] =
    ConfigDecoder.catchNonFatal("Rational")(Rational.apply)

  implicit val realConfigDecoder: ConfigDecoder[Real] =
    ConfigDecoder.catchNonFatal("Real")(Real.apply)

  implicit def safeLongConfigDecoder(
    implicit decoder: ConfigDecoder[BigInt]
  ): ConfigDecoder[SafeLong] =
    decoder.map(SafeLong.apply)

  implicit def trileanConfigDecoder(
    implicit decoder: ConfigDecoder[Option[Boolean]]
  ): ConfigDecoder[Trilean] =
    decoder.map(Trilean.apply)

  implicit def ubyteConfigDecoder(
    implicit decoder: ConfigDecoder[Int]
  ): ConfigDecoder[UByte] =
    decoder.collect("UByte") {
      case int if UByte.MinValue.toInt <= int && int <= UByte.MaxValue.toInt =>
        UByte(int)
    }

  implicit def uintConfigDecoder(
    implicit decoder: ConfigDecoder[Long]
  ): ConfigDecoder[UInt] =
    decoder.collect("UInt") {
      case long if UInt.MinValue.toLong <= long && long <= UInt.MaxValue.toLong =>
        UInt(long)
    }

  implicit def ulongConfigDecoder(
    implicit decoder: ConfigDecoder[BigInt]
  ): ConfigDecoder[ULong] =
    decoder.collect("ULong") {
      case bigInt if ULong.MinValue.toBigInt <= bigInt && bigInt <= ULong.MaxValue.toBigInt =>
        ULong.fromBigInt(bigInt)
    }

  implicit def ushortConfigDecoder(
    implicit decoder: ConfigDecoder[Int]
  ): ConfigDecoder[UShort] =
    decoder.collect("UShort") {
      case int if UShort.MinValue.toInt <= int && int <= UShort.MaxValue.toInt =>
        UShort(int)
    }
}
