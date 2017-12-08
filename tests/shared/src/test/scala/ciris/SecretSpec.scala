package ciris

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

final class SecretSpec extends PropertySpec {
  "Secret" should {
    "use a placeholder when represented as a string" in {
      forAll { secret: Secret[Int] =>
        secret.toString shouldBe "Secret(***)"
      }
    }

    "be equal to a copy with the same value" in {
      forAll { secret: Secret[Int] =>
        secret shouldBe secret.copy()
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
  }

  implicit def arbSecret[A: Arbitrary]: Arbitrary[Secret[A]] =
    Arbitrary(arbitrary[A].map(Secret.apply))
}
