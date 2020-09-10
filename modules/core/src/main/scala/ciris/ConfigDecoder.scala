/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{MonadError, Show}
import cats.implicits._
import scala.annotation.tailrec
import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * Decodes configuration values from a first type to a second type.
  */
sealed abstract class ConfigDecoder[A, B] {

  /**
    * Returns a new [[ConfigDecoder]] which attempts to decode
    * values to the specified type.
    */
  final def as[C](implicit decoder: ConfigDecoder[B, C]): ConfigDecoder[A, C] =
    mapEither(decoder.decode)

  /**
    * Returns a new [[ConfigDecoder]] which successfully decodes
    * values for which the specified partial function is defined.
    */
  final def collect[C](typeName: String)(f: PartialFunction[B, C])(
    implicit show: Show[B]
  ): ConfigDecoder[A, C] =
    ConfigDecoder.instance { (key, value) =>
      decode(key, value).flatMap { b =>
        f.andThen(_.asRight)
          .applyOrElse(
            b,
            (b: B) => Left(ConfigError.decode(typeName, key, b))
          )
      }
    }

  /**
    * Attempts to decode the specified value to the second type.
    *
    * The key may be used for improved error messages. The key
    * is present for a single configuration value, and missing
    * for default values and composed values.
    *
    * @see [[ConfigError.decode]] for creating decode errors
    */
  def decode(key: Option[ConfigKey], value: A): Either[ConfigError, B]

  /**
    * Returns a new [[ConfigDecoder]] using the specified
    * function whenever the value is successfully decoded.
    */
  final def flatMap[C](f: B => ConfigDecoder[A, C]): ConfigDecoder[A, C] =
    ConfigDecoder.instance { (key, value) =>
      decode(key, value).flatMap { a => f(a).decode(key, value) }
    }

  /**
    * Returns a new [[ConfigDecoder]] which applies the
    * specified function on successfully decoded values.
    */
  final def map[C](f: B => C): ConfigDecoder[A, C] =
    ConfigDecoder.instance { (key, value) => decode(key, value).map(f) }

  /**
    * Returns a new [[ConfigDecoder]] which successfully decodes
    * values for which the specified function returns `Right`.
    */
  final def mapEither[C](f: (Option[ConfigKey], B) => Either[ConfigError, C]): ConfigDecoder[A, C] =
    ConfigDecoder.instance { (key, value) => decode(key, value).flatMap(f(key, _)) }

  /**
    * Returns a new [[ConfigDecoder]] which successfully decodes
    * values for which the specified function returns `Some`.
    */
  final def mapOption[C](typeName: String)(f: B => Option[C])(
    implicit show: Show[B]
  ): ConfigDecoder[A, C] =
    mapEither { (key, b) => f(b).toRight(ConfigError.decode(typeName, key, b)) }

  /**
    * Returns a new [[ConfigDecoder]] which redacts
    * sensitive details from error messages.
    */
  final def redacted: ConfigDecoder[A, B] =
    ConfigDecoder.instance { (key, value) => decode(key, value).leftMap(_.redacted) }
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Decoders Decoder Instances
  * @groupprio Decoders 1
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 2
  */
final object ConfigDecoder {

  /**
    * Returns a new [[ConfigDecoder]] for the specified type
    * without performing any kind of decoding.
    *
    * @group Create
    */
  final def identity[A]: ConfigDecoder[A, A] =
    identityConfigDecoder

  /**
    * Returns a new [[ConfigDecoder]] for the specified type
    * without performing any kind of decoding. Alias for the
    * [[ConfigDecoder.identity]] function.
    *
    * @group Create
    */
  final def apply[A]: ConfigDecoder[A, A] =
    identity

  /**
    * Returns a [[ConfigDecoder]] instance between the two
    * specified types if an instance is available.
    *
    * @group Create
    */
  final def apply[A, B](implicit decoder: ConfigDecoder[A, B]): ConfigDecoder[A, B] =
    decoder

  /**
    * @group Decoders
    */
  implicit final val stringBigDecimalConfigDecoder: ConfigDecoder[String, BigDecimal] =
    ConfigDecoder[String].mapOption("BigDecimal") { s =>
      try {
        Some(BigDecimal(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringBigIntConfigDecoder: ConfigDecoder[String, BigInt] =
    ConfigDecoder[String].mapOption("BigInt") { s =>
      try {
        Some(BigInt(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringBooleanConfigDecoder: ConfigDecoder[String, Boolean] =
    ConfigDecoder[String].mapOption("Boolean") { s =>
      if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on"))
        Some(true)
      else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off"))
        Some(false)
      else
        None
    }

  /**
    * @group Decoders
    */
  implicit final val stringByteConfigDecoder: ConfigDecoder[String, Byte] =
    ConfigDecoder[String].mapOption("Byte") { s =>
      try {
        Some(s.toByte)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringCharConfigDecoder: ConfigDecoder[String, Char] =
    ConfigDecoder[String].collect("Char") {
      case s if s.length == 1 => s.charAt(0)
    }

  /**
    * @group Decoders
    */
  implicit final val stringDoubleConfigDecoder: ConfigDecoder[String, Double] =
    ConfigDecoder[String].mapOption("Double") { s =>
      try {
        Some {
          if (s.lastOption.exists(_ == '%')) {
            s.init.toDouble / 100d
          } else {
            s.toDouble
          }
        }
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringDurationConfigDecoder: ConfigDecoder[String, Duration] =
    ConfigDecoder[String].mapOption("Duration") { s =>
      try {
        Some(Duration(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringFiniteDurationConfigDecoder: ConfigDecoder[String, FiniteDuration] =
    ConfigDecoder[String, Duration].collect("FiniteDuration") { case finite: FiniteDuration =>
      finite
    }

  /**
    * @group Decoders
    */
  implicit final val stringFloatConfigDecoder: ConfigDecoder[String, Float] =
    ConfigDecoder[String].mapOption("Float") { s =>
      try {
        Some {
          if (s.lastOption.exists(_ == '%')) {
            s.init.toFloat / 100f
          } else {
            s.toFloat
          }
        }
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final def identityConfigDecoder[A]: ConfigDecoder[A, A] =
    ConfigDecoder.lift(_.asRight)

  /**
    * Returns a new [[ConfigDecoder]] which decodes values
    * using the specified function, with access to the key.
    *
    * If the decode function does not need access to the key,
    * then we can use [[ConfigDecoder.lift]] instead.
    *
    * @group Create
    */
  final def instance[A, B](
    decode: (Option[ConfigKey], A) => Either[ConfigError, B]
  ): ConfigDecoder[A, B] = {
    val _decode = decode
    new ConfigDecoder[A, B] {
      override final def decode(key: Option[ConfigKey], value: A): Either[ConfigError, B] =
        _decode(key, value)

      override final def toString: String =
        "ConfigDecoder$" + System.identityHashCode(this)
    }
  }

  /**
    * @group Decoders
    */
  implicit final val stringIntConfigDecoder: ConfigDecoder[String, Int] =
    ConfigDecoder[String].mapOption("Int") { s =>
      try {
        Some(s.toInt)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * Returns a new [[ConfigDecoder]] which decodes values
    * using the specified function.
    *
    * If the decode function needs access to the key, then
    * we can use [[ConfigDecoder.instance]] instead.
    *
    * @group Create
    */
  final def lift[A, B](decode: A => Either[ConfigError, B]): ConfigDecoder[A, B] =
    ConfigDecoder.instance((_, value) => decode(value))

  /**
    * @group Decoders
    */
  implicit final val stringLongConfigDecoder: ConfigDecoder[String, Long] =
    ConfigDecoder[String].mapOption("Long") { s =>
      try {
        Some(s.toLong)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Decoders
    */
  implicit final val stringShortConfigDecoder: ConfigDecoder[String, Short] =
    ConfigDecoder[String].mapOption("Short") { s =>
      try {
        Some(s.toShort)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Instances
    */
  implicit final def configDecoderMonadError[A]: MonadError[ConfigDecoder[A, ?], ConfigError] =
    new MonadError[ConfigDecoder[A, ?], ConfigError] {
      override final def flatMap[B, C](
        decoder: ConfigDecoder[A, B]
      )(f: B => ConfigDecoder[A, C]): ConfigDecoder[A, C] =
        decoder.flatMap(f)

      override final def map[B, C](decoder: ConfigDecoder[A, B])(f: B => C): ConfigDecoder[A, C] =
        decoder.map(f)

      override final def pure[B](b: B): ConfigDecoder[A, B] =
        ConfigDecoder.lift(_ => Right(b))

      override final def handleErrorWith[B](
        decoder: ConfigDecoder[A, B]
      )(f: ConfigError => ConfigDecoder[A, B]): ConfigDecoder[A, B] =
        ConfigDecoder.instance { (key, value) =>
          decoder.decode(key, value).handleErrorWith { error => f(error).decode(key, value) }
        }

      override final def raiseError[B](error: ConfigError): ConfigDecoder[A, B] =
        ConfigDecoder.lift(_ => Left(error))

      override final def tailRecM[B, C](b: B)(
        f: B => ConfigDecoder[A, Either[B, C]]
      ): ConfigDecoder[A, C] =
        ConfigDecoder.instance { (key, value) =>
          @tailrec def go(decoder: ConfigDecoder[A, Either[B, C]]): Either[ConfigError, C] =
            decoder.decode(key, value) match {
              case left @ Left(_)          => left.asInstanceOf[Either[ConfigError, C]]
              case Right(right @ Right(_)) => right.asInstanceOf[Either[ConfigError, C]]
              case Right(Left(b))          => go(f(b))
            }

          go(f(b))
        }
    }
}
