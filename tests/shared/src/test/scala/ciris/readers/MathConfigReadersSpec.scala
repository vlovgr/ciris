package ciris.readers

import ciris.PropertySpec

import java.math.{BigDecimal => JBigDecimal}
import java.math.{BigInteger => JBigInteger}

final class MathConfigReadersSpec extends PropertySpec {
  "MathConfigReaders" when {
    "reading a BigInt" should {
      "successfully read BigInt values" in {
        forAll { bigInt: BigInt =>
          readValue[BigInt](bigInt.toString) shouldBe Right(bigInt)
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(BigInt(string))) {
            readValue[BigInt](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a BigDecimal" should {
      "successfully read BigDecimal values" in {
        forAll { double: Double =>
          val doubleString = double.toString
          readValue[BigDecimal](doubleString) shouldBe Right(BigDecimal(doubleString))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(BigDecimal(string))) {
            readValue[BigDecimal](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Java BigDecimal" should {
      "successfully read BigDecimal values" in {
        forAll { double: Double =>
          val doubleString = double.toString
          readValue[JBigDecimal](doubleString) shouldBe Right(new JBigDecimal(doubleString))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(new JBigDecimal(string))) {
            readValue[JBigDecimal](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a Java BigInteger" should {
      "successfully read BigInteger values" in {
        forAll { bigInt: BigInt =>
          val bigIntString = bigInt.toString()
          readValue[JBigInteger](bigIntString) shouldBe Right(new JBigInteger(bigIntString))
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(new JBigInteger(string))) {
            readValue[JBigDecimal](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
