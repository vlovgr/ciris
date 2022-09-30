/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import cats.effect.IO
import cats.syntax.all._
import ciris._
import enumeratum.CirisEnumSpec.Suit
import enumeratum.EnumEntry.Lowercase
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.effect.PropF
import org.scalacheck.Gen

final class CirisEnumSpec extends CatsEffectSuite with ScalaCheckEffectSuite {
  test("enum.success") {
    val gen = Gen.oneOf(Suit.values)
    PropF.forAllF(gen) { entry =>
      default(entry.entryName)
        .as[Suit]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("enum.error") {
    val names = Suit.values.map(_.entryName)
    val gen = arbitrary[String].filterNot(names.contains)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[Suit]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Suit"))))
        .assertEquals(Left(true))
    }
  }
}

object CirisEnumSpec {
  sealed trait Suit extends EnumEntry with Lowercase

  object Suit extends Enum[Suit] with CirisEnum[Suit] {
    case object Clubs extends Suit
    case object Diamonds extends Suit
    case object Hearts extends Suit
    case object Spades extends Suit

    val values = findValues
  }
}
