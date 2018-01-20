package ciris.spire.decoders

import _root_.spire.math._
import ciris._
import ciris.spire._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

final class SpireConfigDecodersSpec extends PropertySpec {
  "SpireConfigDecoders" when {
    "reading Algebraic" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigDecoder[String, Algebraic].decode(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Interval[Rational]" should {
      val decoder = ConfigDecoder[String, Interval[Rational]]
      def shouldSucceed(value: String) =
        decoder.decode(existingEntry(value)) shouldBe a[Right[_, _]]

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
          ConfigDecoder[String, Natural].decode(existingEntry(digitString)) shouldBe a[Right[_, _]]
        }
      }

      "fail to read non digit-only strings" in {
        forAll { string: String =>
          whenever(string.exists(c => !c.isDigit)) {
            ConfigDecoder[String, Natural].decode(existingEntry(string)) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading Number" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigDecoder[String, Number].decode(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Polynomial[Rational]" should {
      "successfully read an example polynomial" in {
        ConfigDecoder[String, Polynomial[Rational]]
          .decode(existingEntry("1/5x + 1/3x^2")) shouldBe a[Right[_, _]]
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
          ConfigDecoder[String, Rational].decode(existingEntry(rationalString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Real" should {
      "successfully read all BigDecimal strings" in {
        forAll { bigDecimal: BigDecimal =>
          ConfigDecoder[String, Real].decode(existingEntry(bigDecimal.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading SafeLong" should {
      "successfully read all BigInt strings" in {
        forAll { bigInt: BigInt =>
          ConfigDecoder[String, SafeLong].decode(existingEntry(bigInt.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading Trilean" should {
      "successfully read missing value as unknown" in {
        ConfigDecoder[String, Trilean].decode(nonExistingEntry) shouldBe Right(Trilean.Unknown)
      }

      "successfully read boolean values" in {
        val gen = Gen.oneOf(mixedCase("true"), mixedCase("false"))
        forAll(gen) { booleanString =>
          ConfigDecoder[String, Trilean].decode(existingEntry(booleanString)) shouldBe Right {
            if (booleanString.toBoolean) Trilean.True else Trilean.False
          }
        }
      }
    }

    "reading UByte" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UByte.MinValue.toInt, UByte.MaxValue.toInt)
        forAll(gen) { value =>
          ConfigDecoder[String, UByte].decode(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading UInt" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UInt.MinValue.toLong, UInt.MaxValue.toLong)
        forAll(gen) { value =>
          ConfigDecoder[String, UInt].decode(existingEntry(value.toString)) shouldBe a[Right[_, _]]
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
          ConfigDecoder[String, ULong].decode(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading UShort" should {
      "be able to read all representable values" in {
        val gen = Gen.chooseNum(UShort.MinValue.toInt, UShort.MaxValue.toInt)
        forAll(gen) { value =>
          ConfigDecoder[String, UShort].decode(existingEntry(value.toString)) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
