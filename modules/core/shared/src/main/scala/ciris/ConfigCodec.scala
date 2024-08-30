/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.Show
import cats.syntax.all._
import scala.concurrent.duration.{Duration, FiniteDuration}
import cats.Invariant
import scala.annotation.nowarn

/**
  * Decodes/Encodes configuration values.
  */
sealed abstract class ConfigCodec[A, B] {

  /**
    * Returns a new [[ConfigCodec]] which attempts to decode
    * values to the specified type.
    */
  final def as[C](implicit codec: ConfigCodec[B, C]): ConfigCodec[A, C] =
    imapEither(codec.decode)(codec.encode)

  /**
    * Returns a new [[ConfigCodec]] which successfully decodes
    * values for which the specified partial function is defined.
    */
  final def icollect[C](typeName: String)(f: PartialFunction[B, C])(g: C => B)(
    implicit show: Show[B]
  ): ConfigCodec[A, C] =
    ConfigCodec.instance[A, C] { (key, value) =>
      decode(key, value).flatMap { b =>
        f.andThen(_.asRight)
          .applyOrElse(
            b,
            (b: B) => Left(ConfigError.decode(typeName, key, b))
          )
      }
    }(g andThen encode)

  /**
    * Returns a new [[ConfigCodec]] which applies the
    * specified function on the value before decoding.
    */
  final def icontramap[C](f: C => A)(g: A => C): ConfigCodec[C, B] =
    ConfigCodec.instance[C, B]((key, value) => decode(key, f(value)))(g compose encode)

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
    * Encode the specified value of type `B` to `A`.
    */
  def encode(value: B): A

  /**
    * Returns a new [[ConfigCodec]] which applies the
    * specified function on successfully decoded values.
    */
  final def imap[C](f: B => C)(g: C => B): ConfigCodec[A, C] =
    ConfigCodec.instance[A, C]((key, value) => decode(key, value).map(f))(g andThen encode)

  /**
    * Returns a new [[ConfigCodec]] which successfully decodes
    * values for which the specified function returns `Right`.
    */
  final def imapEither[C](
    f: (Option[ConfigKey], B) => Either[ConfigError, C]
  )(g: C => B): ConfigCodec[A, C] =
    ConfigCodec.instance[A, C]((key, value) => decode(key, value).flatMap(f(key, _)))(
      g andThen encode
    )

  /**
    * Returns a new [[ConfigCodec]] which successfully decodes
    * values for which the specified function returns `Some`.
    */
  final def imapOption[C](typeName: String)(f: B => Option[C])(g: C => B)(
    implicit show: Show[B]
  ): ConfigCodec[A, C] =
    imapEither((key, b) => f(b).toRight(ConfigError.decode(typeName, key, b)))(g)

  /**
    * Returns a new [[ConfigCodec]] which redacts
    * sensitive details from error messages.
    */
  final def redacted: ConfigCodec[A, B] =
    ConfigCodec.instance[A, B]((key, value) => decode(key, value).leftMap(_.redacted))(encode)
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Codecs Decoder Instances
  * @groupprio Codecs 1
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 2
  */
object ConfigCodec {

  /**
    * Returns a new [[ConfigCodec]] for the specified type
    * without performing any kind of decoding.
    *
    * @group Create
    */
  final def identity[A]: ConfigCodec[A, A] =
    identityConfigCodec

  /**
    * Returns a new [[ConfigCodec]] for the specified type
    * without performing any kind of decoding. Alias for the
    * [[ConfigCodec.identity]] function.
    *
    * @group Create
    */
  final def apply[A]: ConfigCodec[A, A] =
    identity

  /**
    * Returns a [[ConfigCodec]] instance between the two
    * specified types if an instance is available.
    *
    * @group Create
    */
  final def apply[A, B](implicit codec: ConfigCodec[A, B]): ConfigCodec[A, B] =
    codec

  private def fromStringOption[A](typeName: String)(
    f: String => Option[A]
  ): ConfigCodec[String, A] =
    ConfigCodec[String].imapOption(typeName)(f)(_.toString)

  /**
    * @group Codecs
    */
  implicit final val stringBigDecimalConfigCodec: ConfigCodec[String, BigDecimal] =
    fromStringOption("BigDecimal")(s =>
      try {
        Some(BigDecimal(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    )

  /**
    * @group Codecs
    */
  implicit final val stringBigIntConfigCodec: ConfigCodec[String, BigInt] =
    fromStringOption("BigInt") { s =>
      try {
        Some(BigInt(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Codecs
    */
  implicit final val stringBooleanConfigCodec: ConfigCodec[String, Boolean] =
    fromStringOption("Boolean") { s =>
      if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on"))
        Some(true)
      else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off"))
        Some(false)
      else
        None
    }

  /**
    * @group Codecs
    */
  implicit final val stringByteConfigCodec: ConfigCodec[String, Byte] =
    fromStringOption("Byte") { s =>
      try {
        Some(s.toByte)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Codecs
    */
  implicit final val stringCharConfigCodec: ConfigCodec[String, Char] =
    fromStringOption("Char") {
      case s if s.length == 1 => Some(s.charAt(0))
      case _                  => None
    }

  /**
    * @group Codecs
    */
  implicit final val stringDoubleConfigCodec: ConfigCodec[String, Double] =
    fromStringOption("Double") { s =>
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
    * @group Codecs
    */
  implicit final val stringDurationConfigCodec: ConfigCodec[String, Duration] =
    fromStringOption("Duration") { s =>
      try {
        Some(Duration(s))
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Codecs
    */
  implicit final val stringFiniteDurationConfigCodec: ConfigCodec[String, FiniteDuration] =
    ConfigCodec[String, Duration].icollect("FiniteDuration") { case finite: FiniteDuration =>
      finite
    }(x => x)

  /**
    * @group Codecs
    */
  implicit final val stringFloatConfigCodec: ConfigCodec[String, Float] =
    fromStringOption("Float") { s =>
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
    * @group Codecs
    */
  implicit final def identityConfigCodec[A]: ConfigCodec[A, A] =
    ConfigCodec.lift[A, A](_.asRight)(x => x)

  /**
    * @group Codecs
    */
  implicit final def secretConfigCodec[A, B](
    implicit codec: ConfigCodec[A, B],
    showA: Show[A]
  ): ConfigCodec[Secret[A], B] =
    codec.icontramap[Secret[A]](_.value)(Secret.apply)

  /**
    * Returns a new [[ConfigCodec]] which decodes values
    * using the specified function, with access to the key.
    *
    * If the decode function does not need access to the key,
    * then we can use [[ConfigCodec.lift]] instead.
    *
    * @group Create
    */
  final def instance[A, B](
    decode: (Option[ConfigKey], A) => Either[ConfigError, B]
  )(encode: B => A): ConfigCodec[A, B] = {
    val _decode = decode
    val _encode = encode
    new ConfigCodec[A, B] {
      override final def decode(key: Option[ConfigKey], value: A): Either[ConfigError, B] =
        _decode(key, value)

      override final def encode(value: B): A =
        _encode(value)

      override final def toString: String =
        "ConfigCodec$" + System.identityHashCode(this)
    }
  }

  /**
    * Returns a new [[ConfigCodec]] which decodes values
    * using the specified function.
    *
    * If the decode function needs access to the key, then
    * we can use [[ConfigCodec.instance]] instead.
    *
    * @group Create
    */
  final def lift[A, B](decode: A => Either[ConfigError, B])(encode: B => A): ConfigCodec[A, B] =
    ConfigCodec.instance[A, B]((_, value) => decode(value))(encode)

  /**
    * Returns a new [[ConfigCodec]] which decodes values
    * using the specified pure function.
    *
    * @group Create
    */
  final def pure[A, B](decode: A => B)(encode: B => A): ConfigCodec[A, B] =
    ConfigCodec.lift[A, B](decode andThen Right.apply)(encode)

  /**
    * @group Codecs
    */
  implicit final val stringIntConfigCodec: ConfigCodec[String, Int] =
    fromStringOption("Int") { s =>
      try {
        Some(s.toInt)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Codecs
    */
  implicit final val stringLongConfigCodec: ConfigCodec[String, Long] =
    fromStringOption("Long") { s =>
      try {
        Some(s.toLong)
      } catch {
        case _: NumberFormatException =>
          None
      }
    }

  /**
    * @group Codecs
    */
  implicit final val stringShortConfigCodec: ConfigCodec[String, Short] =
    fromStringOption("Short") { s =>
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
  implicit final def configCodecInvariant[A]: Invariant[ConfigCodec[A, *]] =
    new Invariant[ConfigCodec[A, *]] {
      override final def imap[B, C](codec: ConfigCodec[A, B])(f: B => C)(
        g: C => B
      ): ConfigCodec[A, C] =
        codec.imap(f)(g)
    }

  /**
    * @group Instances
    */
  @nowarn("cat=deprecation")
  implicit final def decoderFromCodec[A, B](
    implicit codec: ConfigCodec[A, B]
  ): ConfigDecoder[A, B] = ConfigDecoder.instance(codec.decode)
}
