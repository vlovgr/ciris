package ciris

import ciris.ConfigError.{ReadException, MissingKey, WrongType}

final class ConfigErrorsSpec extends PropertySpec {
  "ConfigErrors" when {
    "converting to String" should {
      "list the errors" in {
        val configErrors =
          ConfigErrors(MissingKey("key", ConfigKeyType.Environment))
            .append(ConfigError("a"))

        configErrors.toString shouldBe "ConfigErrors(MissingKey(key,Environment),ConfigError(a))"
      }
    }

    "converting to messages" should {
      "list the error messages" in {
        val configErrors =
          ConfigErrors(MissingKey("key", ConfigKeyType.Environment))
            .append(ReadException("key2", ConfigKeyType.Properties, new Error("error")))
            .append(WrongType("key3", "value3", "Int", ConfigKeyType.Environment, cause = None))
            .append(ConfigError("a"))
            .append(ConfigError.combined(ConfigError("b"), ConfigError("c")))

        configErrors.messages shouldBe Vector(
          "Missing environment variable [key]",
          "Exception while reading system property [key2]: java.lang.Error: error",
          "Environment variable [key3] with value [value3] cannot be converted to type [Int]",
          "a",
          "b, c"
        )
      }
    }
  }
}
