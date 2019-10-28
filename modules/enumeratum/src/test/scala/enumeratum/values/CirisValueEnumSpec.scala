package enumeratum.values

import cats.effect.{ContextShift, IO}
import cats.implicits._
import ciris._
import enumeratum.values.CirisValueEnumSpec._
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.util.Try

final class CirisValueEnumSpec extends AnyFunSuite with ScalaCheckPropertyChecks {
  implicit val contextShift: ContextShift[IO] =
    IO.contextShift(concurrent.ExecutionContext.global)

  test("byteEnum.success") {
    val gen = Gen.oneOf(CustomByteEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomByteEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("byteEnum.error") {
    val values = CustomByteEnum.values.map(_.value)
    forAll { byte: Byte =>
      whenever(!values.contains(byte)) {
        assert {
          val actual =
            default(byte.show).as[CustomByteEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $byte to CustomByteEnum",
                s"Unable to convert value ${Secret(byte).show} to CustomByteEnum"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("byteEnum.error non") {
    forAll { value: String =>
      whenever(Try(value.toByte).isFailure) {
        assert {
          val actual =
            default(value).as[CustomByteEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $value to Byte",
                s"Unable to convert value ${Secret(value).show} to Byte"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("charEnum.success") {
    val gen = Gen.oneOf(CustomCharEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomCharEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("charEnum.error") {
    val values = CustomCharEnum.values.map(_.value)
    forAll { char: Char =>
      whenever(!values.contains(char)) {
        assert {
          val actual =
            default(char.show).as[CustomCharEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $char to CustomCharEnum",
                s"Unable to convert value ${Secret(char).show} to CustomCharEnum"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("charEnum.error non") {
    forAll { value: String =>
      whenever(value.length != 1) {
        assert {
          val actual =
            default(value).as[CustomCharEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $value to Char",
                s"Unable to convert value ${Secret(value).show} to Char"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("intEnum.success") {
    val gen = Gen.oneOf(CustomIntEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomIntEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("intEnum.error") {
    val values = CustomIntEnum.values.map(_.value)
    forAll { int: Int =>
      whenever(!values.contains(int)) {
        assert {
          val actual =
            default(int.show).as[CustomIntEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $int to CustomIntEnum",
                s"Unable to convert value ${Secret(int).show} to CustomIntEnum"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("intEnum.error non") {
    forAll { value: String =>
      whenever(Try(value.toInt).isFailure) {
        assert {
          val actual =
            default(value).as[CustomIntEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $value to Int",
                s"Unable to convert value ${Secret(value).show} to Int"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("longEnum.success") {
    val gen = Gen.oneOf(CustomLongEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomLongEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("longEnum.error") {
    val values = CustomLongEnum.values.map(_.value)
    forAll { long: Long =>
      whenever(!values.contains(long)) {
        assert {
          val actual =
            default(long.show).as[CustomLongEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $long to CustomLongEnum",
                s"Unable to convert value ${Secret(long).show} to CustomLongEnum"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("longEnum.error non") {
    forAll { value: String =>
      whenever(Try(value.toLong).isFailure) {
        assert {
          val actual =
            default(value).as[CustomLongEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $value to Long",
                s"Unable to convert value ${Secret(value).show} to Long"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("shortEnum.success") {
    val gen = Gen.oneOf(CustomShortEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomShortEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("shortEnum.error") {
    val values = CustomShortEnum.values.map(_.value)
    forAll { short: Short =>
      whenever(!values.contains(short)) {
        assert {
          val actual =
            default(short.show).as[CustomShortEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $short to CustomShortEnum",
                s"Unable to convert value ${Secret(short).show} to CustomShortEnum"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("shortEnum.error non") {
    forAll { value: String =>
      whenever(Try(value.toShort).isFailure) {
        assert {
          val actual =
            default(value).as[CustomShortEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $value to Short",
                s"Unable to convert value ${Secret(value).show} to Short"
              )
            }

          actual == expected
        }
      }
    }
  }

  test("stringEnum.success") {
    val gen = Gen.oneOf(CustomStringEnum.values)
    forAll(gen) { entry =>
      assert {
        val actual =
          default(entry.value.show).as[CustomStringEnum].attempt[IO].unsafeRunSync

        val expected =
          Right(entry)

        actual == expected
      }
    }
  }

  test("stringEnum.error") {
    val values = CustomStringEnum.values.map(_.value)
    forAll { string: String =>
      whenever(!values.contains(string)) {
        assert {
          val actual =
            default(string.show).as[CustomStringEnum].attempt[IO].unsafeRunSync

          val expected =
            Left {
              ConfigError.sensitive(
                s"Unable to convert value $string to CustomStringEnum",
                s"Unable to convert value ${Secret(string).show} to CustomStringEnum"
              )
            }

          actual == expected
        }
      }
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
