package ciris.readers

import ciris.PropertySpec

final class PrimitiveConfigReadersSpec extends PropertySpec {
  "PrimitiveConfigReaders" when {
    "reading a Boolean" should {
      "successfully read true values" in {
        forAll(mixedCase("true")) { string ⇒
          readValue[Boolean](string) shouldBe Right(true)
        }
      }

      "successfully read false values" in {
        forAll(mixedCase("false")) { string ⇒
          readValue[Boolean](string) shouldBe Right(false)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toBoolean)) {
            readValue[Boolean](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Byte" should {
      "successfully read Byte values" in {
        forAll { byte: Byte ⇒
          readValue[Byte](byte.toString) shouldBe Right(byte)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toByte)) {
            readValue[Byte](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Char" should {
      "successfully read Char values" in {
        forAll { char: Char ⇒
          readValue[Char](char.toString) shouldBe Right(char)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(string.length != 1) {
            readValue[Char](string.toString) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Double" should {
      "successfully read Double values" in {
        forAll { double: Double ⇒
          readValue[Double](double.toString) shouldBe Right(double)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toDouble)) {
            readValue[Double](string.toString) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Float" should {
      "successfully read Float values" in {
        forAll { float: Float ⇒
          readValue[Float](float.toString) shouldBe Right(float)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toFloat)) {
            readValue[Float](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading an Int" should {
      "successfully read Int values" in {
        forAll { int: Int ⇒
          readValue[Int](int.toString) shouldBe Right(int)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toInt)) {
            readValue[Int](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Long" should {
      "successfully read Long values" in {
        forAll { long: Long ⇒
          readValue[Long](long.toString) shouldBe Right(long)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toLong)) {
            readValue[Int](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Short" should {
      "successfully read Short values" in {
        forAll { short: Short ⇒
          readValue[Short](short.toString) shouldBe Right(short)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(string.toShort)) {
            readValue[Short](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a String" should {
      "successfully read String values" in {
        forAll { string: String ⇒
          readValue[String](string) shouldBe Right(string)
        }
      }
    }
  }
}
