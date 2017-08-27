package ciris.spire.readers

import _root_.spire.math._
import ciris._
import ciris.spire._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

final class SpireConfigReadersSpec extends PropertySpec {
  "SpireConfigReaders" when {
    "reading Algebraic" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigReader[Algebraic].read(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Interval[Rational]" should {
      val reader = ConfigReader[Interval[Rational]]
      def shouldSucceed(value: String) =
        reader.read(existingEntry(value)) shouldBe a[Right[_, _]]

      "successfully read Interval.all" in {
        shouldSucceed("(-∞, ∞)")
      }

      "successfully read valid intervals" in {
        forAll { (a: BigInt, b: Option[BigInt]) =>
          whenever(!b.exists(_ == 0)) {
            val s = s"$a${b.map(b => s"/$b").getOrElse("")}"

            shouldSucceed(s"(-∞, $s)")
            shouldSucceed(s"(-∞, $s]")
            shouldSucceed(s"($s, ∞)")
            shouldSucceed(s"[$s, ∞)")
            shouldSucceed(s"[$s, $s]")
            shouldSucceed(s"($s, $s)")
            shouldSucceed(s"[$s, $s)")
            shouldSucceed(s"($s, $s]")
          }
        }
      }
    }

    "reading Natural" should {
      "successfully read digit-only strings" in {
        val gen = for {
          size <- Gen.chooseNum(1, 100)
          string <- Gen.listOfN(size, Gen.chooseNum(0, 9))
        } yield string.mkString

        forAll(gen) { digitString =>
          ConfigReader[Natural].read(existingEntry(digitString)) shouldBe a[Right[_, _]]
        }
      }

      "fail to read non digit-only strings" in {
        forAll { string: String =>
          whenever(string.exists(c => !c.isDigit)) {
            ConfigReader[Natural].read(existingEntry(string)) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading Number" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigReader[Number].read(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Polynomial[Rational]" should {
      "successfully read an example polynomial" in {
        ConfigReader[Polynomial[Rational]]
          .read(existingEntry("1/5x + 1/3x^2")) shouldBe a[Right[_, _]]
      }
    }

    "reading Rational" should {
      "successfully read rationals on the form d/n" in {
        val gen = for {
          n <- arbitrary[BigInt]
          d <- Gen.option(arbitrary[BigInt])
          if !d.exists(_ == 0)
        } yield s"$n${d.map(d => "/" + d).getOrElse("")}"

        forAll(gen) { rationalString =>
          ConfigReader[Rational].read(existingEntry(rationalString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Real" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigReader[Real].read(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading SafeLong" should {
      "successfully read all BigInt strings" in {
        forAll { bigInt: BigInt =>
          ConfigReader[SafeLong].read(existingEntry(bigInt.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Trilean" should {
      "successfully read missing value as unknown" in {
        ConfigReader[Trilean].read(nonExistingEntry) shouldBe Right(Trilean.Unknown)
      }

      "successfully read boolean values" in {
        val gen = Gen.oneOf(mixedCase("true"), mixedCase("false"))
        forAll(gen) { booleanString =>
          ConfigReader[Trilean].read(existingEntry(booleanString)) shouldBe Right {
            if (booleanString.toBoolean) Trilean.True else Trilean.False
          }
        }
      }
    }

    "reading UByte" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UByte.MinValue.toInt, UByte.MaxValue.toInt)
        forAll(gen) { value =>
          ConfigReader[UByte].read(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading UInt" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UInt.MinValue.toLong, UInt.MaxValue.toLong)
        forAll(gen) { value =>
          ConfigReader[UInt].read(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading ULong" should {
      "be able to read all representable values" in {
        val gen = for {
          first <- Gen.chooseNum(0L, Long.MaxValue)
          second <- Gen.chooseNum(0L, Long.MaxValue)
          zeroOrOne <- Gen.choose(0L, 1L)
        } yield
          BigInt(first) + BigInt(second) + zeroOrOne // ULong.MaxValue is 2 * Long.MaxValue + 1

        forAll(gen) { value =>
          ConfigReader[ULong].read(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading UShort" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UShort.MinValue.toInt, UShort.MaxValue.toInt)
        forAll(gen) { value =>
          ConfigReader[UShort].read(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
