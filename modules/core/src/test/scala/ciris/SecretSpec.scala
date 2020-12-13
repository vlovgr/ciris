package ciris

import cats.implicits._
import cats.kernel.laws.discipline.EqTests
import org.apache.commons.codec.digest.DigestUtils.sha1Hex

final class SecretSpec extends BaseSpec {
  checkAll("Secret", EqTests[Secret[String]].eqv)

  test("Secret.equals.other") {
    forAll { (secret: Secret[String]) =>
      assert((secret: Any) != secret.value)
    }
  }

  test("Secret.equals.secret") {
    forAll { (first: Secret[String], second: Secret[String]) =>
      val expected = first.value == second.value
      val actual = first == second
      assert(actual === expected)
    }
  }

  test("Secret.hashCode") {
    forAll { (secret: Secret[String]) =>
      assert(secret.hashCode === secret.value.hashCode)
    }
  }

  test("Secret.show") {
    forAll { (secret: Secret[String]) =>
      assert(secret.show === secret.toString)
    }
  }

  test("Secret.toString") {
    forAll { (secret: Secret[String]) =>
      assert(secret.toString === s"Secret(${secret.valueShortHash})")
    }
  }

  test("Secret.unapply") {
    forAll { (secret: Secret[String]) =>
      assert {
        secret match {
          case Secret(value) => value === secret.value
        }
      }
    }
  }

  test("Secret.value") {
    forAll { (value: String) =>
      assert(Secret(value).value === value)
    }
  }

  test("Secret.valueHash") {
    forAll { (secret: Secret[String]) =>
      assert(secret.valueHash === sha1Hex(secret.value.show))
    }
  }

  test("Secret.valueShortHash") {
    forAll { (secret: Secret[String]) =>
      assert(secret.valueShortHash === secret.valueHash.substring(0, 7))
    }
  }
}
