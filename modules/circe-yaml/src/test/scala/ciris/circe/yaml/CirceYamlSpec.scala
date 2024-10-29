/*
 * Copyright 2017-2024 Viktor Rudebeck
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
  test("circeYamlConfigCodec.success") {
    default("123")
      .asIso[Int](circeYamlConfigCodec("Int"))
      .load[IO]
      .assertEquals(123)
  }

  test("circeYamlConfigCodec.success.noquotes") {
    default("abc")
      .asIso[String](circeYamlConfigCodec("String"))
      .load[IO]
      .assertEquals("abc")
  }

  test("circeYamlConfigCodec.success.quotes") {
    default("\"abc\"")
      .asIso[String](circeYamlConfigCodec("String"))
      .load[IO]
      .assertEquals("abc")
  }

  test("circeYamlConfigCodec.invalid.noquotes") {
    checkError(
      ConfigValue.default("abc").asIso[Int](circeYamlConfigCodec("Int")),
      """Unable to decode json "abc" to Int: DecodingFailure at : Int"""
    )
  }

  test("circeYamlConfigCodec.invalid") {
    checkError(
      ConfigValue.default("\"abc\"").asIso[Int](circeYamlConfigCodec("Int")),
      """Unable to decode json "abc" to Int: DecodingFailure at : Int"""
    )
  }

  test("circeYamlConfigCodec.invalid.redacted") {
    checkError(
      ConfigValue.default("\"abc\"").asIso[Int](circeYamlConfigCodec("Int")).redacted,
      "Unable to decode json to Int"
    )
  }

  test("circeYamlConfigCodec.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").asIso[Int](circeYamlConfigCodec("Int")),
      """Key with json "abc" cannot be decoded to Int: DecodingFailure at : Int"""
    )
  }

  test("circeYamlConfigCodec.invalid.loaded.redacted") {
    checkError(
      ConfigValue
        .loaded(ConfigKey("key"), "\"abc\"")
        .asIso[Int](circeYamlConfigCodec("Int"))
        .redacted,
      "Key cannot be decoded to Int"
    )
  }

  test("yamlConfigCodec.success") {
    default("123")
      .asIso[Json]
      .load[IO]
      .map(_.asNumber.flatMap(_.toInt).contains(123))
      .assert
  }

  test("yamlConfigCodec.invalid") {
    checkError(
      ConfigValue.default("\"no\"").asIso[Boolean],
      "Unable to convert value \"no\" to Boolean"
    )
  }

  test("yamlConfigCodec.invalid.redacted") {
    checkError(
      ConfigValue.default("\"no\"").asIso[Boolean].redacted,
      "Unable to convert value to Boolean"
    )
  }

  test("yamlConfigCodec.invalid.loaded") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"no\"").asIso[Boolean],
      "Key with value \"no\" cannot be converted to Boolean"
    )
  }

  test("yamlConfigCodec.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"no\"").asIso[Boolean].redacted,
      "Key cannot be converted to Boolean"
    )
  }

  def checkError[A](value: ConfigValue[IO, A], message: String) =
    value
      .attempt[IO]
      .flatMap {
        case Left(error) =>
          IO(
            assert(
              error.messages === Chain.one(message),
              s"Got messages:\n\n${error.messages}\n\nExpected message:\n\n$message"
            )
          )
        case Right(a) => IO(fail(s"expected error, got $a"))
      }
}
