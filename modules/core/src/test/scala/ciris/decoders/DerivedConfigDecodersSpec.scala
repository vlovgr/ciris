package ciris.decoders

import ciris.{ConfigError, ConfigEntry, ConfigKeyType}
import ciris.ConfigError.left
import ciris.PropertySpec
import org.scalacheck.Gen

final class DerivedConfigDecodersSpec extends PropertySpec {
  "DerivedConfigDecoders" when {
    "decoding an Option" when {
      "the key exists" should {
        "keep the type conversion error if there is one" in {
          forAll(Gen.alphaLowerStr) { s =>
            readValue[Option[Int]](s) shouldBe a[Left[_, _]]
          }
        }

        "wrap the value in a Some if type conversion succeeds" in {
          forAll { n: Int =>
            readValue[Option[Int]](n.toString) shouldBe Right(Some(n))
          }
        }
      }

      "the key is missing" should {
        "return a None" in {
          readNonExistingValue[Option[Int]] shouldBe Right(None)
        }
      }

      "the key cannot be retrieved" should {
        "keep the error" in {
          val keyType = ConfigKeyType[String]("keyType")
          val value = left[String](ConfigError("error"))

          ConfigEntry("key", keyType, value)
            .decodeValue[Option[Int]]
            .value shouldBe value
        }
      }
    }
  }
}
