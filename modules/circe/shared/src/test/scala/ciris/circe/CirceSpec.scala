/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.circe

import cats.effect.IO
import cats.syntax.all._
import ciris._
import io.circe.Json
import munit.CatsEffectSuite

final class CirceSpec extends CatsEffectSuite {
  test("circeConfigDecoder.success") {
    default("123").as[Int](circeConfigDecoder("Int")).load[IO].assertEquals(123)

  }

  test("circeConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeConfigDecoder("Int")),
      """Unable to decode json "abc" to Int"""
    )
  }

  test("circeConfigDecoder.invalid.redacted") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeConfigDecoder("Int")).redacted,
      "Unable to decode json to Int"
    )
  }

  test("circeConfigDecoder.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeConfigDecoder("Int")),
      """Key with json "abc" cannot be decoded to Int"""
    )
  }

  test("circeConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeConfigDecoder("Int")).redacted,
      "Key cannot be decoded to Int"
    )
  }

  test("jsonConfigDecoder.success") {
    default("123")
      .as[Json]
      .load[IO]
      .map(_.asNumber.flatMap(_.toInt).contains(123))
      .assert
  }

  test("jsonConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("abc").as[Json],
      "Unable to parse value abc as json"
    )
  }

  test("jsonConfigDecoder.invalid.redacted") {
    checkError(
      ConfigValue.default("abc").as[Json].redacted,
      "Unable to parse value as json"
    )
  }

  test("jsonConfigDecoder.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "abc").as[Json],
      "Key with value abc cannot be parsed as json"
    )
  }

  test("jsonConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "abc").as[Json].redacted,
      "Key cannot be parsed as json"
    )
  }

  def checkError[A](value: ConfigValue[IO, A], message: String): IO[Unit] =
    value
      .attempt[IO]
      .flatMap {
        case Left(error) =>
          val exists =
            error.messages.exists(_.contains(message))

          val printHelpText =
            IO.println(
              s"""
              |Assertion could not find an error message containing:
              |
              |  $message
              |
              |The available error messages were:
              |
              |${error.messages.map("  " ++ _).toList.mkString("\n")}
             """.stripMargin.trim
            ).unlessA(exists)

          val assertion =
            IO(assert(exists))

          printHelpText >> assertion
        case Right(a) =>
          IO(fail(s"expected error, got $a"))
      }
}
