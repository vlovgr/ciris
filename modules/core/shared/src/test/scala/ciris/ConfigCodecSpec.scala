/*
 * Copyright 2017-2024 Viktor Rudebeck
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

final class ConfigCodecSpec extends DisciplineSuite with Generators {
  implicit def configCodecEq[A, B](
    implicit arb: Arbitrary[A],
    eq: Eq[B]
  ): Eq[ConfigCodec[A, B]] =
    Eq.instance { (d1, d2) =>
      Test.check {
        forAll { (a: A) =>
          d1.decode(None, a) === d2.decode(None, a)
        }
      }(_ => scalaCheckTestParameters).passed
    }

  property("ConfigCodec.as") {
    forAll { (key: Option[ConfigKey], s: String) =>
      val expected = ConfigCodec[String, Int].decode(key, s)
      val actual = ConfigCodec[String].as[Int].decode(key, s)
      actual === expected
    }
  }

  property("ConfigCodec.bigDecimal.success") {
    forAll { (bigDecimal: BigDecimal) =>
      ConfigCodec[String, BigDecimal].decode(None, bigDecimal.toString) === bigDecimal.asRight
    }
  }

  property("ConfigCodec.bigDecimal.failure") {
    val gen =
      arbitrary[String].filter(s => Try(BigDecimal(s)).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, BigDecimal].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.bigInt.success") {
    forAll { (bigInt: BigInt) =>
      ConfigCodec[String, BigInt].decode(None, bigInt.toString) === bigInt.asRight
    }
  }

  property("ConfigCodec.bigInt.failure") {
    val gen =
      arbitrary[String].filter(s => Try(BigInt(s)).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, BigInt].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.boolean.success") {
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
      ConfigCodec[String, Boolean].decode(None, boolean.toString).isRight
    }
  }

  property("ConfigCodec.boolean.failure") {
    def isBoolean(s: String) =
      s.toLowerCase() match {
        case "true" | "yes" | "on"  => true
        case "false" | "no" | "off" => true
        case _                      => false
      }

    val gen =
      arbitrary[String].filterNot(isBoolean)

    forAll(gen) { s =>
      ConfigCodec[String, Boolean].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.byte.success") {
    forAll { (byte: Byte) =>
      ConfigCodec[String, Byte].decode(None, byte.toString) === byte.asRight
    }
  }

  property("ConfigCodec.byte.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toByte).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Byte].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.char.success") {
    forAll { (char: Char) =>
      ConfigCodec[String, Char].decode(None, char.toString) === char.asRight
    }
  }

  property("ConfigCodec.char.failure") {
    val gen =
      arbitrary[String].filter(_.length =!= 1)

    forAll(gen) { s =>
      ConfigCodec[String, Char].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.double.success") {
    forAll { (double: Double) =>
      ConfigCodec[String, Double].decode(None, double.toString) === double.asRight
    }
  }

  property("ConfigCodec.double.success percent") {
    forAll { (double: Double) =>
      ConfigCodec[String, Double]
        .decode(None, double.toString ++ "%") === (double / 100f).asRight
    }
  }

  property("ConfigCodec.double.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toDouble).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Double].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.duration.success") {
    val gen =
      arbitrary[Duration]
        .filterNot(_ eq Duration.Undefined)
        .filter(duration => Try(Duration(duration.toString)).isSuccess)

    forAll(gen) { duration =>
      val expected = Right(Duration(duration.toString))
      ConfigCodec[String, Duration].decode(None, duration.toString) == expected
    }
  }

  property("ConfigCodec.duration.failure") {
    val gen =
      arbitrary[String].filter(s => Try(Duration(s)).isFailure)

    forAll(gen) { s =>
      assert(ConfigCodec[String, Duration].decode(None, s).isLeft)
    }
  }

  property("ConfigCodec.finiteDuration.success") {
    val gen =
      arbitrary[FiniteDuration]
        .filter(d => Try(Duration(d.toString)).isSuccess)

    forAll { (finiteDuration: FiniteDuration) =>
      val actual = ConfigCodec[String, FiniteDuration].decode(None, finiteDuration.toString)
      val expected = Right(Duration(finiteDuration.toString))
      actual == expected
    }
  }

  property("ConfigCodec.finiteDuration.failure") {
    val gen =
      arbitrary[String].filter(s => Try(Duration(s)).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, FiniteDuration].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.float.success") {
    forAll { (float: Float) =>
      ConfigCodec[String, Float].decode(None, float.toString) === float.asRight
    }
  }

  property("ConfigCodec.float.success percent") {
    forAll { (float: Float) =>
      ConfigCodec[String, Float].decode(None, float.toString ++ "%") === (float / 100f).asRight
    }
  }

  property("ConfigCodec.float.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toFloat).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Float].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.int.success") {
    forAll { (int: Int) =>
      ConfigCodec[String, Int].decode(None, int.toString) === int.asRight
    }
  }

  property("ConfigCodec.int.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toInt).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Int].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.long.success") {
    forAll { (long: Long) =>
      ConfigCodec[String, Long].decode(None, long.toString) === long.asRight
    }
  }

  property("ConfigCodec.long.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toLong).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Long].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.secret.success") {
    forAll { (int: Int) =>
      ConfigCodec[Secret[String], Int].decode(None, Secret(int.show)) === int.asRight
    }
  }

  property("ConfigCodec.secret.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toInt).isFailure)

    forAll(gen) { s =>
      ConfigCodec[Secret[String], Int].decode(None, Secret(s)).isLeft
    }
  }

  property("ConfigCodec.short.success") {
    forAll { (short: Short) =>
      ConfigCodec[String, Short].decode(None, short.toString) === short.asRight
    }
  }

  property("ConfigCodec.short.failure") {
    val gen =
      arbitrary[String].filter(s => Try(s.toShort).isFailure)

    forAll(gen) { s =>
      ConfigCodec[String, Short].decode(None, s).isLeft
    }
  }

  property("ConfigCodec.toString") {
    forAll { (codec: ConfigCodec[String, String]) =>
      codec.toString.startsWith("ConfigCodec$")
    }
  }

  property("ConfigCodec.redacted.nonSensitive") {
    forAll { (key: Option[ConfigKey], value: String) =>
      val codec =
        ConfigCodec.instance[String, String]((key, value) => Left(ConfigError("message")))(identity)

      val expected = "message"
      val actual = codec.redacted.decode(key, value)
      actual.leftMap(_.messages) == Left(Chain.one(expected))
    }
  }

  property("ConfigCodec.redacted.sensitive") {
    forAll { (key: Option[ConfigKey], value: String) =>
      val codec =
        ConfigCodec.instance[String, String]((key, value) => Left(ConfigError.sensitive("message", "redacted")))(identity)
        

      val expected = "redacted"
      val actual = codec.redacted.decode(key, value)
      actual.leftMap(_.messages) == Left(Chain.one(expected))
    }
  }
}
