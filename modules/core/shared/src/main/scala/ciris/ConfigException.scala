/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.{Eq, Foldable, Show}
import cats.syntax.all._

final case class ConfigException(error: ConfigError) extends RuntimeException {
  final def message: String =
    ConfigException.message(error)

  override final def getMessage(): String =
    message

  override final def fillInStackTrace(): Throwable =
    this
}

object ConfigException {
  implicit final val configExceptionEq: Eq[ConfigException] =
    Eq.by(_.error)

  implicit final val configExceptionShow: Show[ConfigException] =
    Show.fromToString

  private[ciris] final val messageLeading: String =
    "configuration loading failed with the following errors.\n"

  private[ciris] final val messageTrailing: String =
    "\n"

  private[ciris] final val entryLeading: String =
    "\n  - "

  private[ciris] final val entryNewLineLeading: String =
    " " * (entryLeading.length - 1)

  private[ciris] final val entryTrailing: String =
    "."

  private[ciris] final def formatMessage(message: String): String =
    message.replaceAll("\n", s"\n$entryNewLineLeading")

  private[ciris] final def message(error: ConfigError): String = {
    val messages =
      error.messages.map(formatMessage)

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

  private[ciris] final def messageLength[F[_]: Foldable](messages: F[String]): Int =
    messages.foldMap { message =>
      message.length + entryLeading.length + {
        if (message.endsWith(entryTrailing)) 0
        else entryTrailing.length
      }
    } + messageLeading.length + messageTrailing.length
}
