/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.circe.yaml

import cats.data.Chain
import cats.effect.IO
import cats.syntax.all._
import ciris._
import io.circe.Json
import io.circe.yaml._
import io.circe.yaml.syntax._
import munit.CatsEffectSuite

final class CirceYamlSpec extends CatsEffectSuite {
  test("circeYamlConfigDecoder.success") {
    default("123")
      .as[Int](circeYamlConfigDecoder("Int"))
      .load[IO]
      .assertEquals(123)
  }

  test("circeYamlConfigDecoder.success.noquotes") {
    default("abc")
      .as[String](circeYamlConfigDecoder("String"))
      .load[IO]
      .assertEquals("abc")
  }

  test("circeYamlConfigDecoder.success.quotes") {
    default("\"abc\"")
      .as[String](circeYamlConfigDecoder("String"))
      .load[IO]
      .assertEquals("abc")
  }

  test("circeYamlConfigDecoder.invalid.noquotes") {
    checkError(
      ConfigValue.default("abc").as[Int](circeYamlConfigDecoder("Int")),
      """Unable to decode json "abc" to Int: Int"""
    )
  }

  test("circeYamlConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeYamlConfigDecoder("Int")),
      """Unable to decode json "abc" to Int: Int"""
    )
  }

  test("circeYamlConfigDecoder.invalid.redacted") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeYamlConfigDecoder("Int")).redacted,
      "Unable to decode json to Int"
    )
  }

  test("circeYamlConfigDecoder.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeYamlConfigDecoder("Int")),
      """Key with json "abc" cannot be decoded to Int: Int"""
    )
  }

  test("circeYamlConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue
        .loaded(ConfigKey("key"), "\"abc\"")
        .as[Int](circeYamlConfigDecoder("Int"))
        .redacted,
      "Key cannot be decoded to Int"
    )
  }

  test("yamlConfigDecoder.success") {
    default("123")
      .as[Json]
      .load[IO]
      .map(_.asNumber.flatMap(_.toInt).contains(123))
      .assert
  }

  test("yamlConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("\"no\"").as[Boolean],
      "Unable to convert value \"no\" to Boolean"
    )
  }

  test("yamlConfigDecoder.invalid.redacted") {
    checkError(
      ConfigValue.default("\"no\"").as[Boolean].redacted,
      "Unable to convert value to Boolean"
    )
  }

  test("yamlConfigDecoder.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"no\"").as[Boolean],
      "Key with value \"no\" cannot be converted to Boolean"
    )
  }

  test("yamlConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"no\"").as[Boolean].redacted,
      "Key cannot be converted to Boolean"
    )
  }

  def checkError[A](value: ConfigValue[IO, A], message: String) =
    value
      .attempt[IO]
      .flatMap {
        case Left(error) => IO(assert(error.messages === Chain.one(message)))
        case Right(a)    => IO(fail(s"expected error, got $a"))
      }
}
