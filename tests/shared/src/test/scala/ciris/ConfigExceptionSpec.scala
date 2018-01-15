package ciris

final class ConfigExceptionSpec extends PropertySpec {
  "ConfigException" when {
    "converting to String" should {
      "include the errors" in {
        val configException =
          ConfigErrors(ConfigError.missingKey("key", ConfigKeyType.Environment))
            .append(ConfigError.readException("key", ConfigKeyType.Property, new Error("error")))
            .toException

        configException.toString shouldBe s"ciris.ConfigException: ${configException.getMessage}"
      }
    }

    "retrieving the message" when {
      "there is a single error" should {
        "list the single error" in {
          val configException =
            ConfigErrors(ConfigError.missingKey("key", ConfigKeyType.Environment)).toException

          configException.getMessage.trim shouldBe
            """
              |configuration loading failed with the following errors.
              |
              |  - Missing environment variable [key].
            """.stripMargin.trim
        }
      }

      "there are multiple errors" should {
        "list all the errors without duplicate trailing dots" in {
          val configException =
            ConfigErrors(ConfigError.missingKey("key", ConfigKeyType.Environment))
              .append(ConfigError.readException("key", ConfigKeyType.Property, new Error("error.")))
              .toException

          configException.getMessage.trim shouldBe
            """
              |configuration loading failed with the following errors.
              |
              |  - Missing environment variable [key].
              |  - Exception while reading system property [key]: java.lang.Error: error.
            """.stripMargin.trim
        }
      }
    }
  }
}
