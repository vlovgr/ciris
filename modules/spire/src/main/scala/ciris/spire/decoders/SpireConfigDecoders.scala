package ciris.spire.decoders

import ciris.ConfigDecoder
import spire.math._

import scala.util.{Success, Try}

trait SpireConfigDecoders {
  implicit val algebraicConfigDecoder: ConfigDecoder[String, Algebraic] =
    ConfigDecoder.catchNonFatal("Algebraic")(Algebraic.apply)

  implicit val intervalRationalConfigDecoder: ConfigDecoder[String, Interval[Rational]] =
    ConfigDecoder.catchNonFatal("Interval[Rational]")(Interval.apply)

  implicit val naturalConfigDecoder: ConfigDecoder[String, Natural] =
    ConfigDecoder.fromTryOption("Natural") { value =>
      if (value.forall(_.isDigit))
        Try(Some(Natural(value)))
      else
        Success(None)
    }

  implicit val numberConfigDecoder: ConfigDecoder[String, Number] =
    ConfigDecoder.catchNonFatal("Number")(Number.apply)

  implicit val polynomialRationalConfigDecoder: ConfigDecoder[String, Polynomial[Rational]] =
    ConfigDecoder.catchNonFatal("Polynomial[Rational]")(Polynomial.apply)

  implicit val rationalConfigDecoder: ConfigDecoder[String, Rational] =
    ConfigDecoder.catchNonFatal("Rational")(Rational.apply)

  implicit val realConfigDecoder: ConfigDecoder[String, Real] =
    ConfigDecoder.catchNonFatal("Real")(Real.apply)

  implicit def safeLongConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, BigInt]
  ): ConfigDecoder[A, SafeLong] =
    decoder.map(SafeLong.apply)

  implicit def trileanConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, Option[Boolean]]
  ): ConfigDecoder[A, Trilean] =
    decoder.map(Trilean.apply)

  implicit def ubyteConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, Int]
  ): ConfigDecoder[A, UByte] =
    decoder.collect("UByte") {
      case int if UByte.MinValue.toInt <= int && int <= UByte.MaxValue.toInt =>
        UByte(int)
    }

  implicit def uintConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, Long]
  ): ConfigDecoder[A, UInt] =
    decoder.collect("UInt") {
      case long if UInt.MinValue.toLong <= long && long <= UInt.MaxValue.toLong =>
        UInt(long)
    }

  implicit def ulongConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, BigInt]
  ): ConfigDecoder[A, ULong] =
    decoder.collect("ULong") {
      case bigInt if ULong.MinValue.toBigInt <= bigInt && bigInt <= ULong.MaxValue.toBigInt =>
        ULong.fromBigInt(bigInt)
    }

  implicit def ushortConfigDecoder[A](
    implicit decoder: ConfigDecoder[A, Int]
  ): ConfigDecoder[A, UShort] =
    decoder.collect("UShort") {
      case int if UShort.MinValue.toInt <= int && int <= UShort.MaxValue.toInt =>
        UShort(int)
    }
}
