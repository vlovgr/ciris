package ciris

import scala.util.Try

final class ConfigSourceSpec extends PropertySpec {
  "ConfigSource" when {
    "converting to String" should {
      "include the key type" in {
        forAll { keyType: String ⇒
          val configKey = ConfigKeyType(keyType)
          ConfigSource(configKey)(Right.apply).toString shouldBe s"ConfigSource($configKey)"
        }
      }
    }

    "created from a Try" should {
      "succeed if the Try succeeds" in {
        forAll { (keyType: String, key: String) ⇒
          val source = ConfigSource.fromTry(ConfigKeyType(keyType))(key ⇒ Try(key))
          source.read(key) shouldBe Right(key)
        }
      }

      "fail if the Try fails" in {
        forAll { (keyType: String, key: String) ⇒
          val source = ConfigSource.fromTry(ConfigKeyType(keyType))(_ ⇒ Try(throw new Error))
          source.read(key) shouldBe a[Left[_, _]]
        }
      }
    }

    "catching non-fatal exceptions" should {
      "succeed if an exception is not thrown" in {
        forAll { (keyType: String, key: String) ⇒
          val source = ConfigSource.catchNonFatal(ConfigKeyType(keyType))(identity)
          source.read(key) shouldBe Right(key)
        }
      }

      "fail if a non-fatal exception is thrown" in {
        forAll { (keyType: String, key: String) ⇒
          val source = ConfigSource.catchNonFatal(ConfigKeyType(keyType))(_ ⇒ throw new Error)
          source.read(key) shouldBe a[Left[_, _]]
        }
      }
    }
  }
}
