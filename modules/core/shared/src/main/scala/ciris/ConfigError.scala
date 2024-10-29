/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Show}
import cats.data.Chain
import cats.syntax.all._
import ciris.ConfigError._

/**
  * Error which occurred while loading or decoding configuration values.
  *
  * Configuration errors can be created using [[ConfigError.apply]], or
  * with [[ConfigError.sensitive]] if the error might contain sensitive
  * details. When writing [[ConfigCodec]]s, [[ConfigError.decode]]
  * can be useful for creating decoding errors.
  *
  * Errors for a single configuration value, which might be retrieved
  * from one of multiple sources, can be combined and accumulated with
  * [[ConfigError#or]]. Errors for multiple configuration values can
  * similarly be accumulated using [[ConfigError#and]].
  *
  * Error messages can be retrieved using [[ConfigError#messages]]. If
  * the error relates to a value which might contain sensitive details,
  * [[ConfigError#redacted]] can be used to redact such details. When
  * [[ConfigValue#secret]] is used, sensitive details are redacted and
  * the value is wrapped in [[Secret]] to prevent it from being shown.
  *
  * A `Throwable` representation of a [[ConfigError]] can be retrieved
  * using [[ConfigError#throwable]].
  *
  * @example {{{
  * scala> val error = ConfigError("error")
  * error: ConfigError = ConfigError(error)
  *
  * scala> val sensitive = ConfigError.sensitive("error", "redacted")
  * sensitive: ConfigError = Sensitive(error, redacted)
  *
  * scala> error.or(sensitive).messages
  * res0: cats.data.Chain[String] = Chain(Error and error)
  *
  * scala> error.and(sensitive).redacted.messages
  * res1: cats.data.Chain[String] = Chain(error, redacted)
  * }}}
  */
sealed abstract class ConfigError {

  /**
    * Returns the error messages of the contained errors.
    */
  def messages: Chain[String]

  /**
    * Returns a new [[ConfigError]] with sensitive details redacted.
    */
  def redacted: ConfigError

  /**
    * Returns a new [[ConfigError]] combining errors for
    * separate configuration values.
    */
  final def and(that: ConfigError): ConfigError =
    (this, that) match {
      case (_, Empty)         => this
      case (Empty, _)         => that
      case (And(as), And(bs)) => normalize(as ++ bs, And(_))
      case (And(as), _)       => normalize(as :+ that, And(_))
      case (_, And(bs))       => normalize(this +: bs, And(_))
      case (_, _)             => normalize(Chain(this, that), And(_))
    }

  /**
    * Returns `true` if the error is due to no value
    * being available; otherwise `false`.
    *
    * If the error is a combination of multiple errors,
    * returns `true` if all errors are due to no value
    * being available; otherwise `false`.
    */
  private[ciris] final def isMissing: Boolean =
    this match {
      case And(errors)     => errors.forall(_.isMissing)
      case Apply(_)        => false
      case Empty           => false
      case Loaded          => false
      case Missing(_)      => true
      case Or(errors)      => errors.forall(_.isMissing)
      case Sensitive(_, _) => false
    }

  /**
    * Returns a new [[ConfigError]] combining errors for
    * a single configuration value.
    */
  final def or(that: ConfigError): ConfigError =
    (this, that) match {
      case (_, Empty)       => this
      case (Empty, _)       => that
      case (Or(as), Or(bs)) => normalize(as ++ bs, Or(_))
      case (Or(as), _)      => normalize(as :+ that, Or(_))
      case (_, Or(bs))      => normalize(this +: bs, Or(_))
      case (_, _)           => normalize(Chain(this, that), Or(_))
    }

  /**
    * Returns a new `Throwable` including the contained
    * error messages.
    */
  final def throwable: Throwable =
    ConfigException(this)
}

/**
  * @groupname Create Creating Instances
  * @groupprio Create 0
  *
  * @groupname Instances Type Class Instances
  * @groupprio Instances 1
  */
object ConfigError {

  /**
    * Returns a new [[ConfigError]] using the specified message.
    *
    * If the specified message might contain sensitive details,
    * then use [[ConfigError.sensitive]] instead to create an
    * error which is capable of redacting sensitive details.
    *
    * @group Create
    */
  final def apply(message: => String): ConfigError =
    Apply(() => message)

  /**
    * Returns a new [[ConfigError]] using the specified message and
    * redacted message.
    *
    * The specified message is used in the returned [[ConfigError]].
    * Whenever [[ConfigError#redacted]] is invoked on the returned
    * instance, a new [[ConfigError]] is returned which instead
    * uses the redacted message.
    *
    * @group Create
    */
  final def sensitive(message: => String, redactedMessage: => String): ConfigError =
    Sensitive(() => message, () => redactedMessage)

  /**
    * Returns a new [[ConfigError]] for when the specified value
    * could not be decoded.
    *
    * @group Create
    */
  final def decode[A](
    typeName: String,
    key: Option[ConfigKey],
    value: A
  )(implicit show: Show[A]): ConfigError = {
    def message(valueShown: Option[String]): String =
      (key, valueShown) match {
        case (Some(key), Some(value)) =>
          s"${key.description.capitalize} with value $value cannot be converted to $typeName"
        case (Some(key), None) =>
          s"${key.description.capitalize} cannot be converted to $typeName"
        case (None, Some(value)) =>
          s"Unable to convert value $value to $typeName"
        case (None, None) =>
          s"Unable to convert value to $typeName"
      }

    ConfigError.sensitive(
      message = message(Some(value.show)),
      redactedMessage = message(None)
    )
  }

  private[ciris] final case class And(errors: Chain[ConfigError]) extends ConfigError {
    override final def messages: Chain[String] =
      errors.flatMap(_.messages)

    override final def redacted: ConfigError =
      And(errors.map(_.redacted))

    override final def toString: String =
      s"And(${errors.toList.mkString(", ")})"
  }

  private[ciris] final case class Apply(message: () => String) extends ConfigError {
    override final def messages: Chain[String] =
      Chain.one(message())

    override final val redacted: ConfigError =
      this

    override final def hashCode: Int =
      message().hashCode

    override final def equals(that: Any): Boolean =
      that match {
        case Apply(message2) => message() == message2()
        case _               => false
      }

    override final def toString: String =
      s"ConfigError(${message()})"
  }

  private[ciris] case object Empty extends ConfigError {
    override final val messages: Chain[String] =
      Chain.empty

    override final val redacted: ConfigError =
      this

    override final def toString: String =
      "Empty"
  }

  private[ciris] case object Loaded extends ConfigError {
    override final val messages: Chain[String] =
      Chain.empty

    override final val redacted: ConfigError =
      this

    override final def toString: String =
      "Loaded"
  }

  private[ciris] final case class Missing(key: ConfigKey) extends ConfigError {
    override final def messages: Chain[String] =
      Chain.one(s"Missing ${uncapitalize(key.description)}")

    override final val redacted: ConfigError =
      this

    override final def toString: String =
      s"Missing($key)"
  }

  private[ciris] final case class Or(errors: Chain[ConfigError]) extends ConfigError {
    override final def messages: Chain[String] = {
      val messages =
        errors
          .flatMap(_.messages)
          .toVector
          .zipWithIndex
          .map {
            case (m, 0) => m.capitalize
            case (m, _) => uncapitalize(m)
          }

      Chain.one {
        if (messages.size < 3) {
          messages.mkString(" and ")
        } else {
          messages.init.mkString("", ", ", s", and ${messages.last}")
        }
      }
    }

    override final def redacted: ConfigError =
      Or(errors.map(_.redacted))

    override final def toString: String =
      s"Or(${errors.toList.mkString(", ")})"
  }

  private[ciris] final case class Sensitive(
    message: () => String,
    redactedMessage: () => String
  ) extends ConfigError {
    override final def messages: Chain[String] =
      Chain.one(message())

    override final def redacted: ConfigError =
      Apply(redactedMessage)

    override final def hashCode: Int =
      (message(), redactedMessage()).hashCode

    override final def equals(that: Any): Boolean =
      that match {
        case Sensitive(message2, redactedMessage2) =>
          message() == message2() && redactedMessage() == redactedMessage2()
        case _ =>
          false
      }

    override final def toString: String =
      s"Sensitive(${message()}, ${redactedMessage()})"
  }

  private[ciris] final def normalize(
    errors: Chain[ConfigError],
    create: Chain[ConfigError] => ConfigError
  ): ConfigError =
    if (errors.contains(Loaded)) {
      val rest = errors.filter(_ != Loaded)
      if (rest.isEmpty) {
        Loaded
      } else {
        create(rest.prepend(Loaded))
      }
    } else {
      create(errors)
    }

  private[ciris] final def uncapitalize(s: String): String =
    if (s.length == 0 || s.charAt(0).isLower) s
    else {
      val chars = s.toCharArray
      chars(0) = chars(0).toLower
      new String(chars)
    }

  /**
    * @group Instances
    */
  implicit final val configErrorEq: Eq[ConfigError] = {
    def eqv(e1: Chain[ConfigError], e2: Chain[ConfigError]) =
      Eq[Chain[ConfigError]].eqv(e1, e2)

    Eq.instance {
      case (Empty, Empty)                         => true
      case (Empty, _)                             => false
      case (Loaded, Loaded)                       => true
      case (Loaded, _)                            => false
      case (Missing(k1), Missing(k2))             => k1 === k2
      case (Missing(_), _)                        => false
      case (And(e1), And(e2))                     => eqv(e1, e2)
      case (And(_), _)                            => false
      case (Or(e1), Or(e2))                       => eqv(e1, e2)
      case (Or(_), _)                             => false
      case (Apply(m1), Apply(m2))                 => m1() === m2()
      case (Apply(_), _)                          => false
      case (Sensitive(m1, r1), Sensitive(m2, r2)) => m1() === m2() && r1() === r2()
      case (Sensitive(_, _), _)                   => false
    }
  }

  /**
    * @group Instances
    */
  implicit final val configErrorShow: Show[ConfigError] =
    Show.fromToString
}
