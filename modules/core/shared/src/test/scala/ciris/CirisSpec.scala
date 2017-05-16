package ciris

import org.scalacheck.Gen

final class CirisSpec extends PropertySpec {
  "Ciris" when {
    "loading environment variables" should {
      "be able to load all variables as string" in {
        if (sys.env.nonEmpty) {
          forAll(Gen.oneOf(sys.env.keys.toList)) { key =>
            env[String](key).value shouldBe Right(sys.env(key))
          }
        }
      }

      "return a failure for non-existing variables" in {
        forAll { key: String =>
          whenever(!sys.env.contains(key)) {
            env[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "loading system properties" should {
      "be able to load all properties as string" in {
        if (sys.props.nonEmpty) {
          forAll(Gen.oneOf(sys.props.keys.toList)) { key =>
            prop[String](key).value shouldBe Right(sys.props(key))
          }
        }
      }

      "return a failure for the empty property" in {
        prop[String]("").value shouldBe a[Left[_, _]]
      }

      "return a failure for non-existing properties" in {
        forAll { key: String =>
          whenever(key.nonEmpty && !sys.props.contains(key)) {
            prop[String](key).value shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
