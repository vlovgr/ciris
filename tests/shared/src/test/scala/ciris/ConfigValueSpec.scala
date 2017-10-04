package ciris

final class ConfigValueSpec extends PropertySpec {
  "ConfigValue" when {
    "converting to String" should {
      "include the value" in {
        readConfigValue[String]("value").toString shouldBe "ConfigValue(Right(value))"
      }
    }

    "using orElse" should {
      "use the first value even if the second is available" in {
        val result = ConfigValue(Right(123)) orElse ConfigValue(Right(456))
        result.value shouldBe Right(123)
      }

      "not evaluate the second value if the first is available" in {
        ConfigValue(Right(123)) orElse fail("orElse evaluated second value")
      }

      "use the first value if the second one contains an error" in {
        val result = ConfigValue(Right(123)) orElse ConfigValue(Left(ConfigError("error")))
        result.value shouldBe Right(123)
      }

      "use the second value if first contains an error" in {
        val result = ConfigValue(Left(ConfigError("error"))) orElse ConfigValue(Right(456))
        result.value shouldBe Right(456)
      }

      "combine errors if both contain errors" in {
        val (error1, error2) = (ConfigError("error1"), ConfigError("error2"))
        val result = ConfigValue(Left(error1)) orElse ConfigValue(Left(error2))
        result.value shouldBe Left(ConfigError.combined(error1, error2))
      }
    }
  }
}
