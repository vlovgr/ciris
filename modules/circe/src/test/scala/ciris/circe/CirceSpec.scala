package ciris.circe

import cats.data.Chain
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import ciris._
import io.circe.Json
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite

final class CirceSpec extends AnyFunSuite {
  test("circeConfigDecoder.success") {
    val result = default("123").as[Int](circeConfigDecoder("Int")).load[IO].unsafeRunSync()
    assert(result == 123)
  }

  test("circeConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("\"abc\"").as[Int](circeConfigDecoder("Int")),
      """Unable to decode json "abc" to Int: Int"""
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
      """Key with json "abc" cannot be decoded to Int: Int"""
    )
  }

  test("circeConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "\"abc\"").as[Int](circeConfigDecoder("Int")).redacted,
      "Key cannot be decoded to Int"
    )
  }

  test("jsonConfigDecoder.success") {
    val result = default("123").as[Json].load[IO].unsafeRunSync()
    assert(result.asNumber.flatMap(_.toInt).contains(123))
  }

  test("jsonConfigDecoder.invalid") {
    checkError(
      ConfigValue.default("abc").as[Json],
      "Unable to parse value abc as json: expected json value got 'abc' (line 1, column 1)"
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
      "Key with value abc cannot be parsed as json: expected json value got 'abc' (line 1, column 1)"
    )
  }

  test("jsonConfigDecoder.invalid.loaded.redacted") {
    checkError(
      ConfigValue.loaded(ConfigKey("key"), "abc").as[Json].redacted,
      "Key cannot be parsed as json"
    )
  }

  def checkError[A](value: ConfigValue[IO, A], message: String): Assertion =
    value
      .attempt[IO]
      .flatMap {
        case Left(error) => IO(assert(error.messages === Chain.one(message)))
        case Right(a)    => IO(fail(s"expected error, got $a"))
      }
      .unsafeRunSync()
}
