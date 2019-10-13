/*
 * Copyright 2017-2019 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Foldable}
import cats.implicits._

/**
  * Exception which occurred while loading or decoding configuration values.
  */
sealed abstract class ConfigException(message: String) extends RuntimeException(message) {

  /**
    * Returns the underlying [[ConfigError]] for the exception.
    */
  def error: ConfigError
}

private[ciris] final object ConfigException {
  final val messageLeading: String =
    "configuration loading failed with the following errors.\n"

  final val messageTrailing: String =
    "\n"

  final val entryLeading: String =
    "\n  - "

  final val entryTrailing: String =
    "."

  final def apply(error: ConfigError): ConfigException = {
    val message = {
      val messages =
        error.messages

      val builder =
        new java.lang.StringBuilder(messageLength(messages))
          .append(messageLeading)

      messages.foldLeft(()) { (_, message) =>
        builder.append(entryLeading).append(message)
        if (!message.endsWith(entryTrailing))
          builder.append(entryTrailing)

        ()
      }

      builder.append(messageTrailing).toString
    }

    val _error = error
    new ConfigException(message) {
      override final val error: ConfigError =
        _error

      override final def toString: String =
        s"ciris.ConfigException: $getMessage"
    }
  }

  final def messageLength[F[_]](
    messages: F[String]
  )(implicit F: Foldable[F]): Int = {
    messages.foldMap { message =>
      message.length + entryLeading.length + {
        if (message.endsWith(entryTrailing)) 0
        else entryTrailing.length
      }
    } + messageLeading.length + messageTrailing.length
  }

  final def unapply(exception: ConfigException): Some[ConfigError] =
    Some(exception.error)

  implicit final val configExceptionEq: Eq[ConfigException] =
    Eq.by(_.error)
}
