/*
 * Copyright 2017-2026 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import cats.effect.IO
import cats.syntax.all._
import ciris._
import enumeratum.values.CirisValueEnumSpec._
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.effect.PropF
import org.scalacheck.Gen
import scala.util.Try

final class CirisValueEnumSpec extends CatsEffectSuite with ScalaCheckEffectSuite {
  test("byteEnum.success") {
    val gen = Gen.oneOf(CustomByteEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomByteEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("byteEnum.error") {
    val values = CustomByteEnum.values.map(_.value)
    val gen = arbitrary[Byte].filterNot(values.contains)
    PropF.forAllF(gen) { byte =>
      default(byte.show)
        .as[CustomByteEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomByteEnum"))))
        .assertEquals(Left(true))
    }
  }

  test("byteEnum.error.non") {
    val gen = arbitrary[String].filter(s => Try(s.toByte).isFailure)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomByteEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Byte"))))
        .assertEquals(Left(true))
    }
  }

  test("charEnum.success") {
    val gen = Gen.oneOf(CustomCharEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomCharEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("charEnum.error") {
    val values = CustomCharEnum.values.map(_.value)
    val gen = arbitrary[Char].filterNot(values.contains)
    PropF.forAllF(gen) { char =>
      default(char.show)
        .as[CustomCharEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomCharEnum"))))
        .assertEquals(Left(true))
    }
  }

  test("charEnum.error.non") {
    val gen = arbitrary[String].filter(_.length != 1)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomCharEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Char"))))
        .assertEquals(Left(true))
    }
  }

  test("intEnum.success") {
    val gen = Gen.oneOf(CustomIntEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomIntEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("intEnum.error") {
    val values = CustomIntEnum.values.map(_.value)
    val gen = arbitrary[Int].filterNot(values.contains)
    PropF.forAllF(gen) { int =>
      default(int.show)
        .as[CustomIntEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomIntEnum"))))
        .assertEquals(Left(true))
    }
  }

  test("intEnum.error.non") {
    val gen = arbitrary[String].filter(s => Try(s.toInt).isFailure)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomIntEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Int"))))
        .assertEquals(Left(true))
    }
  }

  test("longEnum.success") {
    val gen = Gen.oneOf(CustomLongEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomLongEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("longEnum.error") {
    val values = CustomLongEnum.values.map(_.value)
    val gen = arbitrary[Long].filterNot(values.contains)
    PropF.forAllF(gen) { long =>
      default(long.show)
        .as[CustomLongEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomLongEnum"))))
        .assertEquals(Left(true))
    }
  }

  test("longEnum.error.non") {
    val gen = arbitrary[String].filter(s => Try(s.toLong).isFailure)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomLongEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Long"))))
        .assertEquals(Left(true))
    }
  }

  test("shortEnum.success") {
    val gen = Gen.oneOf(CustomShortEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomShortEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("shortEnum.error") {
    val values = CustomShortEnum.values.map(_.value)
    val gen = arbitrary[Short].filterNot(values.contains)
    PropF.forAllF(gen) { short =>
      default(short.show)
        .as[CustomShortEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomShortEnum"))))
        .assertEquals(Left(true))
    }
  }

  test("shortEnum.error.non") {
    val gen = arbitrary[String].filter(s => Try(s.toShort).isFailure)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomShortEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("Short"))))
        .assertEquals(Left(true))
    }
  }

  test("stringEnum.success") {
    val gen = Gen.oneOf(CustomStringEnum.values)
    PropF.forAllF(gen) { entry =>
      default(entry.value.show)
        .as[CustomStringEnum]
        .attempt[IO]
        .assertEquals(Right(entry))
    }
  }

  test("stringEnum.error") {
    val values = CustomStringEnum.values.map(_.value)
    val gen = arbitrary[String].filterNot(values.contains)
    PropF.forAllF(gen) { string =>
      default(string)
        .as[CustomStringEnum]
        .attempt[IO]
        .map(_.leftMap(_.messages.exists(_.contains("CustomStringEnum"))))
        .assertEquals(Left(true))
    }
  }
}

object CirisValueEnumSpec {
  sealed abstract class CustomByteEnum(val value: Byte) extends ByteEnumEntry

  object CustomByteEnum extends ByteEnum[CustomByteEnum] with ByteCirisEnum[CustomByteEnum] {
    case object First extends CustomByteEnum(1)
    case object Second extends CustomByteEnum(2)
    case object Third extends CustomByteEnum(3)

    val values = findValues
  }

  sealed abstract class CustomCharEnum(val value: Char) extends CharEnumEntry

  object CustomCharEnum extends CharEnum[CustomCharEnum] with CharCirisEnum[CustomCharEnum] {
    case object First extends CustomCharEnum('1')
    case object Second extends CustomCharEnum('2')
    case object Third extends CustomCharEnum('3')

    val values = findValues
  }

  sealed abstract class CustomIntEnum(val value: Int) extends IntEnumEntry

  object CustomIntEnum extends IntEnum[CustomIntEnum] with IntCirisEnum[CustomIntEnum] {
    case object First extends CustomIntEnum(1)
    case object Second extends CustomIntEnum(2)
    case object Third extends CustomIntEnum(3)

    val values = findValues
  }

  sealed abstract class CustomLongEnum(val value: Long) extends LongEnumEntry

  object CustomLongEnum extends LongEnum[CustomLongEnum] with LongCirisEnum[CustomLongEnum] {
    case object First extends CustomLongEnum(1L)
    case object Second extends CustomLongEnum(2L)
    case object Third extends CustomLongEnum(3L)

    val values = findValues
  }

  sealed abstract class CustomShortEnum(val value: Short) extends ShortEnumEntry

  object CustomShortEnum extends ShortEnum[CustomShortEnum] with ShortCirisEnum[CustomShortEnum] {
    case object First extends CustomShortEnum(1)
    case object Second extends CustomShortEnum(2)
    case object Third extends CustomShortEnum(3)

    val values = findValues
  }

  sealed abstract class CustomStringEnum(val value: String) extends StringEnumEntry

  object CustomStringEnum
      extends StringEnum[CustomStringEnum]
      with StringCirisEnum[CustomStringEnum] {

    case object First extends CustomStringEnum("first")
    case object Second extends CustomStringEnum("second")
    case object Third extends CustomStringEnum("third")

    val values = findValues
  }
}
