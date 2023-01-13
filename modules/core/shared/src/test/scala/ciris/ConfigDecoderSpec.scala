/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.data.Chain
import cats.Eq
import cats.laws.discipline.ContravariantTests
import cats.laws.discipline.MonadErrorTests
import cats.syntax.all._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Test
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.Try

final class ConfigDecoderSpec extends DisciplineSuite with Generators {
  implicit def configDecoderEq[A, B](
    implicit arb: Arbitrary[A],
    eq: Eq[B]
  ): Eq[ConfigDecoder[A, B]] =
    Eq.instance { (d1, d2) =>
      Test.check {
        forAll { (a: A) =>
          d1.decode(None, a) === d2.decode(None, a)
        }
      }(_ => scalaCheckTestParameters).passed
    }

  property("ConfigDecoder.as") {
    forAll { (key: Option[ConfigKey], s: String) =>
      val expected = ConfigDecoder[String, Int].decode(key, s)
      val actual = ConfigDecoder[String].as[Int].decode(key, s)
      actual === expected
    }
  }

  property("ConfigDecoder.bigDecimal.success") {
    forAll { (bigDecimal: BigDecimal) =>
      ConfigDecoder[String, BigDecimal].decode(None, bigDecimal.toString) === bigDecimal.asRight
    }
  }

  property("ConfigDecoder.bigDecimal.failure") {
    val gen =
      arbitrary[String].filter(s => Try(BigDecimal(s)).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, BigDecimal].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.bigInt.success") {
    forAll { (bigInt: BigInt) =>
      ConfigDecoder[String, BigInt].decode(None, bigInt.toString) === bigInt.asRight
    }
  }

  property("ConfigDecoder.bigInt.failure") {
    val gen =
      arbitrary[String].filter(s => Try(BigInt(s)).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, BigInt].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.boolean.success") {
    val genBoolean =
      Gen.oneOf(
        "true",
        "yes",
        "on",
        "false",
        "no",
        "off"
      )

    forAll(genBoolean) { boolean =>
      ConfigDecoder[String, Boolean].decode(None, boolean.toString).isRight
    }
  }

  property("ConfigDecoder.boolean.failure") {
    def isBoolean(s: String) =
      s.toLowerCase() match {
        case "true" | "yes" | "on"  => true
        case "false" | "no" | "off" => true
        case _                      => false
      }

    val gen =
      arbitrary[String].filterNot(isBoolean)

    forAll(gen) { s =>
      ConfigDecoder[String, Boolean].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.byte.success") {
    forAll { (byte: Byte) =>
      ConfigDecoder[String, Byte].decode(None, byte.toString) === byte.asRight
    }
  }

  property("ConfigDecoder.byte.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toByte).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Byte].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.char.success") {
    forAll { (char: Char) =>
      ConfigDecoder[String, Char].decode(None, char.toString) === char.asRight
    }
  }

  property("ConfigDecoder.char.failure") {
    val gen =
      arbitrary[String].filter(_.length =!= 1)

    forAll(gen) { s =>
      ConfigDecoder[String, Char].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.double.success") {
    forAll { (double: Double) =>
      ConfigDecoder[String, Double].decode(None, double.toString) === double.asRight
    }
  }

  property("ConfigDecoder.double.success percent") {
    forAll { (double: Double) =>
      ConfigDecoder[String, Double]
        .decode(None, double.toString ++ "%") === (double / 100f).asRight
    }
  }

  property("ConfigDecoder.double.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toDouble).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Double].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.duration.success") {
    val gen =
      arbitrary[Duration]
        .filterNot(_ eq Duration.Undefined)
        .filter(duration => Try(Duration(duration.toString)).isSuccess)

    forAll(gen) { duration =>
      val expected = Right(Duration(duration.toString))
      ConfigDecoder[String, Duration].decode(None, duration.toString) == expected
    }
  }

  property("ConfigDecoder.duration.failure") {
    val gen =
      arbitrary[String].filter(s => Try(Duration(s)).isFailure)

    forAll(gen) { s =>
      assert(ConfigDecoder[String, Duration].decode(None, s).isLeft)
    }
  }

  property("ConfigDecoder.finiteDuration.success") {
    val gen =
      arbitrary[FiniteDuration]
        .filter(d => Try(Duration(d.toString)).isSuccess)

    forAll { (finiteDuration: FiniteDuration) =>
      val actual = ConfigDecoder[String, FiniteDuration].decode(None, finiteDuration.toString)
      val expected = Right(Duration(finiteDuration.toString))
      actual == expected
    }
  }

  property("ConfigDecoder.finiteDuration.failure") {
    val gen =
      arbitrary[String].filter(s => Try(Duration(s)).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, FiniteDuration].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.float.success") {
    forAll { (float: Float) =>
      ConfigDecoder[String, Float].decode(None, float.toString) === float.asRight
    }
  }

  property("ConfigDecoder.float.success percent") {
    forAll { (float: Float) =>
      ConfigDecoder[String, Float].decode(None, float.toString ++ "%") === (float / 100f).asRight
    }
  }

  property("ConfigDecoder.float.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toFloat).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Float].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.int.success") {
    forAll { (int: Int) =>
      ConfigDecoder[String, Int].decode(None, int.toString) === int.asRight
    }
  }

  property("ConfigDecoder.int.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toInt).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Int].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.long.success") {
    forAll { (long: Long) =>
      ConfigDecoder[String, Long].decode(None, long.toString) === long.asRight
    }
  }

  property("ConfigDecoder.long.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toLong).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Long].decode(None, s).isLeft
    }
  }

  checkAll(
    "ConfigDecoder",
    ContravariantTests[ConfigDecoder[*, String]].contravariant[String, String, String]
  )

  checkAll(
    "ConfigDecoder",
    MonadErrorTests[ConfigDecoder[String, *], ConfigError].monadError[String, String, String]
  )

  property("ConfigDecoder.short.success") {
    forAll { (short: Short) =>
      ConfigDecoder[String, Short].decode(None, short.toString) === short.asRight
    }
  }

  property("ConfigDecoder.short.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toShort).isFailure)

    forAll(gen) { s =>
      ConfigDecoder[String, Short].decode(None, s).isLeft
    }
  }

  property("ConfigDecoder.toString") {
    forAll { (decoder: ConfigDecoder[String, String]) =>
      decoder.toString.startsWith("ConfigDecoder$")
    }
  }

  property("ConfigDecoder.redacted.nonSensitive") {
    forAll { (key: Option[ConfigKey], value: String) =>
      val decoder =
        ConfigDecoder.instance[String, String] { (key, value) =>
          Left(ConfigError("message"))
        }

      val expected = "message"
      val actual = decoder.redacted.decode(key, value)
      actual.leftMap(_.messages) == Left(Chain.one(expected))
    }
  }

  property("ConfigDecoder.redacted.sensitive") {
    forAll { (key: Option[ConfigKey], value: String) =>
      val decoder =
        ConfigDecoder.instance[String, String] { (key, value) =>
          Left(ConfigError.sensitive("message", "redacted"))
        }

      val expected = "redacted"
      val actual = decoder.redacted.decode(key, value)
      actual.leftMap(_.messages) == Left(Chain.one(expected))
    }
  }
}
