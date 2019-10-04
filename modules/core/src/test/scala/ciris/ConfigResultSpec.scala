package ciris

import cats.{Id, MonadError}

final class ConfigResultSpec extends PropertySpec {
  "ConfigResult" when {
    "created with apply" should {
      "include the result in toStringWithResult" in {
        ConfigResult[Id, Int](Right(123)).toStringWithResult shouldBe "ConfigResult(Right(123))"
      }

      "not include the result in toString" in {
        ConfigResult[Id, Int](Right(123)).toString shouldNot be("ConfigResult(Right(123))")
      }
    }

    "using orThrow" should {
      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readConfigEntry[String]("key2")
        )(_ + _)

        noException shouldBe thrownBy {
          config.orThrow()
        }
      }

      "throw an exception if loading failed" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readNonExistingConfigEntry[String]
        )(_ + _)

        a[ConfigException] shouldBe thrownBy {
          config.orThrow()
        }
      }
    }
  }
}
