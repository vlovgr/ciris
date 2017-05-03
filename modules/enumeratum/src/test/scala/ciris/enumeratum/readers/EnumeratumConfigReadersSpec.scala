package ciris.enumeratum.readers

import ciris.PropertySpec
import ciris.enumeratum._
import enumeratum.EnumEntry._
import enumeratum._
import enumeratum.values._
import org.scalacheck.Gen

import scala.collection.immutable.IndexedSeq

final class EnumeratumConfigReadersSpec extends PropertySpec {
  sealed abstract class ByteEnumItem(override val value: Byte, val name: String) extends ByteEnumEntry
  case object ByteEnumItem extends ByteEnum[ByteEnumItem] {
    case object A extends ByteEnumItem(value = 1, name = "a")
    case object B extends ByteEnumItem(value = 2, name = "b")
    case object C extends ByteEnumItem(value = 3, name = "c")
    case object D extends ByteEnumItem(value = 4, name = "d")
    override val values: IndexedSeq[ByteEnumItem] = findValues
  }

  sealed abstract class CharEnumItem(override val value: Char, val name: String) extends CharEnumEntry
  case object CharEnumItem extends CharEnum[CharEnumItem] {
    case object A extends CharEnumItem(value = '1', name = "a")
    case object B extends CharEnumItem(value = '2', name = "b")
    case object C extends CharEnumItem(value = '3', name = "c")
    case object D extends CharEnumItem(value = '4', name = "d")
    override val values: IndexedSeq[CharEnumItem] = findValues
  }

  sealed abstract class EnumEntryItem extends EnumEntry with UpperSnakecase
  object EnumEntryItem extends Enum[EnumEntryItem] {
    case object A extends EnumEntryItem with Uppercase
    case object B extends EnumEntryItem with Lowercase
    case object C extends EnumEntryItem
    val values: IndexedSeq[EnumEntryItem] = findValues
  }

  sealed abstract class IntEnumItem(override val value: Int, val name: String) extends IntEnumEntry
  case object IntEnumItem extends IntEnum[IntEnumItem] {
    case object A extends IntEnumItem(value = 1, name = "a")
    case object B extends IntEnumItem(value = 2, name = "b")
    case object C extends IntEnumItem(value = 3, name = "c")
    case object D extends IntEnumItem(value = 4, name = "d")
    override val values: IndexedSeq[IntEnumItem] = findValues
  }

  sealed abstract class LongEnumItem(override val value: Long, val name: String) extends LongEnumEntry
  case object LongEnumItem extends LongEnum[LongEnumItem] {
    case object A extends LongEnumItem(value = 1L, name = "a")
    case object B extends LongEnumItem(value = 2L, name = "b")
    case object C extends LongEnumItem(value = 3L, name = "c")
    case object D extends LongEnumItem(value = 4L, name = "d")
    override val values: IndexedSeq[LongEnumItem] = findValues
  }

  sealed abstract class ShortEnumItem(override val value: Short, val name: String) extends ShortEnumEntry
  case object ShortEnumItem extends ShortEnum[ShortEnumItem] {
    case object A extends ShortEnumItem(value = 1, name = "a")
    case object B extends ShortEnumItem(value = 2, name = "b")
    case object C extends ShortEnumItem(value = 3, name = "c")
    case object D extends ShortEnumItem(value = 4, name = "d")
    override val values: IndexedSeq[ShortEnumItem] = findValues
  }

  sealed abstract class StringEnumItem(override val value: String, val name: String) extends StringEnumEntry
  case object StringEnumItem extends StringEnum[StringEnumItem] {
    case object A extends StringEnumItem(value = "1", name = "a")
    case object B extends StringEnumItem(value = "2", name = "b")
    case object C extends StringEnumItem(value = "3", name = "c")
    case object D extends StringEnumItem(value = "4", name = "d")
    override val values: IndexedSeq[StringEnumItem] = findValues
  }

  "EnumeratumConfigReaders" when {
    "reading a ByteEnum" should {
      "successfully read the ByteEnum values" in {
        forAll(Gen.oneOf(ByteEnumItem.values)) { enum ⇒
          readValue[ByteEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ByteEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[ByteEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a CharEnum" should {
      "successfully read the CharEnum values" in {
        forAll(Gen.oneOf(CharEnumItem.values)) { enum ⇒
          readValue[CharEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!CharEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[CharEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading an EnumEntry" should {
      "successfully read the EnumEntry names" in {
        forAll(Gen.oneOf(EnumEntryItem.values)) { enum ⇒
          readValue[EnumEntryItem](enum.entryName) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!EnumEntryItem.values.map(_.entryName).contains(string)) {
            readValue[EnumEntryItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading an IntEnum" should {
      "successfully read the IntEnum values" in {
        forAll(Gen.oneOf(IntEnumItem.values)) { enum ⇒
          readValue[IntEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!IntEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[IntEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LongEnum" should {
      "successfully read the LongEnum values" in {
        forAll(Gen.oneOf(LongEnumItem.values)) { enum ⇒
          readValue[LongEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!LongEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[LongEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ShortEnum" should {
      "successfully read the ShortEnum values" in {
        forAll(Gen.oneOf(ShortEnumItem.values)) { enum ⇒
          readValue[ShortEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!ShortEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[ShortEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a StringEnum" should {
      "successfully read the StringEnum values" in {
        forAll(Gen.oneOf(StringEnumItem.values)) { enum ⇒
          readValue[StringEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(!StringEnumItem.values.map(_.value.toString).contains(string)) {
            readValue[StringEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
