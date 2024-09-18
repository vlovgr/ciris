/*
 * Copyright 2017-2024 Viktor Rudebeck
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
  test("circeConfigCodec.success") {
    default("123").as[Int](circeConfigCodec("Int")).load[IO].assertEquals(123)

  }

  test("circeConfigCodec.invalid") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeConfigCodec("Int")),
      """Unable to decode json "abc" to Int"""
    )
  }

  test("circeConfigCodec.invalid.redacted") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeConfigCodec("Int")).redacted,
      "Unable to decode json to Int"
    )
  }

  test("circeConfigCodec.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeConfigCodec("Int")),
      """Key with json "abc" cannot be decoded to Int"""
    )
  }

  test("circeConfigCodec.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeConfigCodec("Int")).redacted,
      "Key cannot be decoded to Int"
    )
  }

  test("jsonConfigCodec.success") {
    default("123")
      .as[Json]
      .load[IO]
      .map(_.asNumber.flatMap(_.toInt).contains(123))
      .assert
  }

  test("jsonConfigCodec.invalid") {
    checkError(
      ConfigValue.default("abc").as[Json],
      "Unable to parse value abc as json"
    )
  }

  test("jsonConfigCodec.invalid.redacted") {
    checkError(
      ConfigValue.default("abc").as[Json].redacted,
      "Unable to parse value as json"
    )
  }

  test("jsonConfigCodec.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "abc").as[Json],
      "Key with value abc cannot be parsed as json"
    )
  }

  test("jsonConfigCodec.invalid.loaded.redacted") {
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
