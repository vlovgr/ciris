package ciris

import org.apache.commons.codec.digest.DigestUtils.sha1Hex
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

final class SecretSpec extends PropertySpec {
  "Secret" should {
    "use the short hash when represented as a string" in {
      forAll { secret: Secret[Int] =>
        secret.toString shouldBe s"Secret(${secret.valueShortHash})"
      }
    }

    "be equal to a copy with the same value" in {
      forAll { secret: Secret[Int] =>
        secret shouldBe secret.copy()
      }
    }

    "not be equal if values are different" in {
      forAll { (a: Int, b: Int) =>
        whenever(a != b) {
          Secret(a) shouldNot equal(Secret(b))
        }
      }
    }

    "not be equal if one is not a secret" in {
      forAll { (a: Int, b: Int) =>
        Secret(a) shouldNot equal(b)
      }
    }

    "have equal hash code to a copy with the same value" in {
      forAll { secret: Secret[Int] =>
        secret.hashCode shouldBe secret.copy().hashCode
      }
    }

    "be able to change type when copying" in {
      forAll { secret: Secret[Int] =>
        secret.copy(secret.value.toString) shouldBe Secret(secret.value.toString)
      }
    }

    "be able to extract the value with pattern matching" in {
      forAll { secret: Secret[Int] =>
        val value = secret match { case Secret(value) => value }
        value shouldBe secret.value
      }
    }

    "be able to read secret values" in {
      forAll { value: Int =>
        readValue[Secret[Int]](value.toString) shouldBe Right(Secret(value))
      }
    }

    "calculate the expected hash" in {
      forAll { s: String =>
        Secret(s).valueHash shouldBe sha1Hex(s)
      }
    }
  }

  implicit def arbSecret[A: Arbitrary]: Arbitrary[Secret[A]] =
    Arbitrary(arbitrary[A].map(Secret.apply))
}
