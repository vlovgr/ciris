package ciris

final class ConfigErrorSpec extends PropertySpec {
  "ConfigError" when {
    "created from a message" should {
      "pass the message by reference" in {
        forAll { message: String =>
          var messageCreated = false

          val error = ConfigError({
            messageCreated = true
            message
          })

          messageCreated shouldBe false
          error.message shouldBe message
          messageCreated shouldBe true
        }
      }
    }

    "converting to String" should {
      "have a String representation for custom errors" in {
        ConfigError("abc").toString shouldBe "ConfigError(abc)"
      }

      "have a String representation for combined errors" in {
        ConfigError.combined(ConfigError("a"), ConfigError("b")).toString shouldBe
          "Combined(ConfigError(a), ConfigError(b))"
      }

      "have a String representation for missing key errors" in {
        val keyType = ConfigKeyType[String]("keyType")
        ConfigError.missingKey("key", keyType).toString shouldBe
          s"MissingKey(key, $keyType)"
      }

      "have a String representation for read exceptions errors" in {
        val keyType = ConfigKeyType[String]("keyType")
        ConfigError.readException("key", keyType, new Error("message")).toString shouldBe
          s"ReadException(key, $keyType, java.lang.Error: message)"
      }

      "have a String representation for wrong type errors without cause" in {
        val keyType = ConfigKeyType[String]("keyType")
        ConfigError.wrongType("key", keyType, Right("sourceValue"), "value", "typeName", None).toString shouldBe
          "WrongType(key, ConfigKeyType(keyType), Right(sourceValue), value, typeName)"
      }

      "have a String representation for wrong type errors with cause" in {
        val keyType = ConfigKeyType[String]("keyType")
        ConfigError.wrongType("key", keyType, Right("value"), "value", "typeName", Some("cause")).toString shouldBe
          "WrongType(key, ConfigKeyType(keyType), Right(value), value, typeName, cause)"
      }
    }
  }
}
