package ciris

import ciris.ConfigError.{readException, missingKey, wrongType}

final class ConfigErrorsSpec extends PropertySpec {
  "ConfigErrors" when {
    "converting to String" should {
      "list the errors" in {
        val configErrors =
          ConfigErrors(missingKey("key", ConfigKeyType.Environment))
            .append(ConfigError("a"))

        configErrors.toString shouldBe "ConfigErrors(MissingKey(key, Environment), ConfigError(a))"
      }
    }

    "converting to messages" should {
      "list the error messages" in {
        val configErrors =
          ConfigErrors(missingKey("key", ConfigKeyType.Environment))
            .append(readException("key2", ConfigKeyType.Property, new Error("error")))
            .append(wrongType("key3", ConfigKeyType.Environment, Right("value3"), "value3", "Int", None))
            .append(wrongType("key4", ConfigKeyType.Environment, Right("value4"), "value5", "Int", None))
            .append(ConfigError("a"))
            .append(ConfigError.combined(ConfigError("bb"), ConfigError("CC")))
            .append(ConfigError.combined(ConfigError("dd"), ConfigError("EE"), ConfigError("FF")))
            .append(ConfigError.combined(ConfigError(""), ConfigError("g")))
            .append(ConfigError.combined(ConfigError(""), ConfigError("")))
            .append(ConfigError.combined(ConfigError(""), ConfigError("hh"), ConfigError("II")))

        configErrors.messages shouldBe Vector(
          "Missing environment variable [key]",
          "Exception while reading system property [key2]: java.lang.Error: error",
          "Environment variable [key3] with value [value3] cannot be converted to type [Int]",
          "Environment variable [key4] with value [value5] (and unmodified value [value4]) cannot be converted to type [Int]",
          "a",
          "Bb and cC",
          "Dd, eE, and fF",
          "G",
          "",
          "Hh and iI"
        )
      }
    }

    "converting to exception" should {
      "be able to retrieve the original errors" in {
        val configErrors =
          ConfigErrors(missingKey("key", ConfigKeyType.Environment))
            .append(readException("key2", ConfigKeyType.Property, new Error("error")))

        configErrors.toException.errors shouldBe configErrors
      }
    }
  }
}
