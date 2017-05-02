package ciris

import ciris.ConfigError.{InvalidKey, MissingKey, WrongType}

final class ConfigErrorsSpec extends PropertySpec {
  "ConfigErrors" when {
    "converting to String" should {
      "list the errors" in {
        val configErrors = ConfigErrors(MissingKey("key", ConfigSource.Environment))
        configErrors.toString shouldBe "ConfigErrors(MissingKey(key,Environment))"
      }
    }

    "converting to messages" should {
      "list the error messages" in {
        val configErrors =
          ConfigErrors(MissingKey("key", ConfigSource.Environment))
            .append(InvalidKey("key2", ConfigSource.Properties, new Error("error")))
            .append(WrongType("key3", "value3", "Int", ConfigSource.Environment, cause = None))

        configErrors.messages shouldBe Vector(
          "Missing environment variable [key]",
          "Invalid system property [key2]: java.lang.Error: error",
          "Environment variable [key3] with value [value3] cannot be converted to type [Int]"
        )
      }
    }
  }
}
