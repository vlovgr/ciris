/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import ciris._
import enumeratum.CirisEnumSpec.Suit
import enumeratum.EnumEntry.Lowercase
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

final class CirisEnumSpec extends AnyFunSuite with ScalaCheckPropertyChecks {
  test("enum.success") {
    val gen = Gen.oneOf(Suit.values)
    forAll(gen) { suit =>
      assert {
        val actual = default(suit.entryName).as[Suit].attempt[IO].unsafeRunSync()
        val expected = Right(suit)
        actual == expected
      }
    }
  }

  test("enum.error") {
    val names = Suit.values.map(_.entryName).toSet
    forAll { (name: String) =>
      whenever(!names.contains(name)) {
        assert {
          default(name).as[Suit].attempt[IO].unsafeRunSync().isLeft
        }
      }
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
