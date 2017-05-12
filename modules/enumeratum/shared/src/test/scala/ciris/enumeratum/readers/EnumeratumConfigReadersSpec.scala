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
      "successfully read ByteEnum values" in {
        forAll(Gen.oneOf(ByteEnumItem.values)) { enum ⇒
          readValue[ByteEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional ByteEnum values" in {
        forAll(Gen.oneOf(ByteEnumItem.values)) { enum ⇒
          readValue[Option[ByteEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { byte: Byte ⇒
          whenever(ByteEnumItem.withValueOpt(byte).isEmpty) {
            readValue[ByteEnumItem](byte.toString) shouldBe a[Left[_, _]]
          }
        }
      }

      "return a failure for wrong type values" in {
        forAll { string: String ⇒
          whenever(fails(string.toByte)) {
            readValue[ByteEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a CharEnum" should {
      "successfully read CharEnum values" in {
        forAll(Gen.oneOf(CharEnumItem.values)) { enum ⇒
          readValue[CharEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional CharEnum values" in {
        forAll(Gen.oneOf(CharEnumItem.values)) { enum ⇒
          readValue[Option[CharEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { char: Char ⇒
          whenever(CharEnumItem.withValueOpt(char).isEmpty) {
            readValue[CharEnumItem](char.toString) shouldBe a[Left[_, _]]
          }
        }
      }

      "return a failure for wrong type values" in {
        forAll { string: String ⇒
          whenever(string.length != 1) {
            readValue[CharEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading an EnumEntry" should {
      "successfully read EnumEntry names" in {
        forAll(Gen.oneOf(EnumEntryItem.values)) { enum ⇒
          readValue[EnumEntryItem](enum.entryName) shouldBe Right(enum)
        }
      }

      "successfully read optional EnumEntry names" in {
        forAll(Gen.oneOf(EnumEntryItem.values)) { enum ⇒
          readValue[Option[EnumEntryItem]](enum.entryName) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(EnumEntryItem.withNameOption(string).isEmpty) {
            readValue[EnumEntryItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading an IntEnum" should {
      "successfully read IntEnum values" in {
        forAll(Gen.oneOf(IntEnumItem.values)) { enum ⇒
          readValue[IntEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional IntEnum values" in {
        forAll(Gen.oneOf(IntEnumItem.values)) { enum ⇒
          readValue[Option[IntEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { int: Int ⇒
          whenever(IntEnumItem.withValueOpt(int).isEmpty) {
            readValue[IntEnumItem](int.toString) shouldBe a[Left[_, _]]
          }
        }
      }

      "return a failure for wrong type values" in {
        forAll { string: String ⇒
          whenever(fails(string.toInt)) {
            readValue[IntEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a LongEnum" should {
      "successfully read LongEnum values" in {
        forAll(Gen.oneOf(LongEnumItem.values)) { enum ⇒
          readValue[LongEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional LongEnum values" in {
        forAll(Gen.oneOf(LongEnumItem.values)) { enum ⇒
          readValue[Option[LongEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { long: Long ⇒
          whenever(LongEnumItem.withValueOpt(long).isEmpty) {
            readValue[LongEnumItem](long.toString) shouldBe a[Left[_, _]]
          }
        }
      }

      "return a failure for wrong type values" in {
        forAll { string: String ⇒
          whenever(fails(string.toLong)) {
            readValue[LongEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a ShortEnum" should {
      "successfully read ShortEnum values" in {
        forAll(Gen.oneOf(ShortEnumItem.values)) { enum ⇒
          readValue[ShortEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional ShortEnum values" in {
        forAll(Gen.oneOf(ShortEnumItem.values)) { enum ⇒
          readValue[Option[ShortEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { short: Short ⇒
          whenever(ShortEnumItem.withValueOpt(short).isEmpty) {
            readValue[ShortEnumItem](short.toString) shouldBe a[Left[_, _]]
          }
        }
      }

      "return a failure for wrong type values" in {
        forAll { string: String ⇒
          whenever(fails(string.toShort)) {
            readValue[ShortEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a StringEnum" should {
      "successfully read StringEnum values" in {
        forAll(Gen.oneOf(StringEnumItem.values)) { enum ⇒
          readValue[StringEnumItem](enum.value.toString) shouldBe Right(enum)
        }
      }

      "successfully read optional StringEnum values" in {
        forAll(Gen.oneOf(StringEnumItem.values)) { enum ⇒
          readValue[Option[StringEnumItem]](enum.value.toString) shouldBe Right(Some(enum))
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(StringEnumItem.withValueOpt(string).isEmpty) {
            readValue[StringEnumItem](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
