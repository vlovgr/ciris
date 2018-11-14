package ciris

final class ConfigErrorSpec extends PropertySpec {
  "ConfigError" when {
    "using append" should {
      "create a ConfigErrors" in {
        val a = ConfigError("a")
        val b = ConfigError("b")
        (a append b).toVector shouldBe Vector(a, b)
      }
    }

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

    "created from a sensitive message" should {
      "pass both messages by reference" in {
        forAll { (message: String, redactedMessage: String) =>
          var messageCreated = false
          var redactedMessageCreated = false

          val error = ConfigError.sensitive({
            messageCreated = true
            message
          }, {
            redactedMessageCreated = true
            redactedMessage
          })

          messageCreated shouldBe false
          redactedMessageCreated shouldBe false
          error.message shouldBe message
          messageCreated shouldBe true
          redactedMessageCreated shouldBe false
          error.toString shouldBe s"ConfigError($message)"
          redactedMessageCreated shouldBe false
          val redactedError = error.redactSensitive
          redactedMessageCreated shouldBe false
          redactedError.message shouldBe redactedMessage
          redactedMessageCreated shouldBe true
          redactedError.toString shouldBe s"ConfigError($redactedMessage)"
        }
      }
    }

    "created from multiple sensitive messages" should {
      "redact sensitive messages" in {
        val redactedErrors =
          ConfigError.combined(
            ConfigError.sensitive("message", "redactedMessage"),
            ConfigError("regularMessage")
          ).redactSensitive

        redactedErrors.message shouldBe "RedactedMessage and regularMessage"
      }
    }

    "redacting wrongType" should {
      "redact source value error, value, and cause" in {
        val keyType = ConfigKeyType[String]("keyType")
        val sourceValue = ConfigError.left[String](ConfigError.sensitive("message", ConfigError.redactedValue))
        val error = ConfigError.wrongType("key", keyType, sourceValue, "value", "typeName", Some("cause"))
        error.redactSensitive.message shouldBe "KeyType [key] with value [<redacted>] cannot be converted to type [typeName]"
      }

      "redact source value, value, and cause" in {
        val keyType = ConfigKeyType[String]("keyType")
        val sourceValue = ConfigError.right("sourceValue")
        val error = ConfigError.wrongType("key", keyType, sourceValue, "value", "typeName", Some("cause"))
        error.redactSensitive.message shouldBe "KeyType [key] with value [<redacted>] cannot be converted to type [typeName]"
      }
    }

    "using isMissingKey" should {
      val missingKey =
        ConfigError.missingKey("key", ConfigKeyType[String]("keyType"))

      "return true for MissingKey" in {
        missingKey.isMissingKey shouldBe true
      }

      "return true for a combination of MissingKeys" in {
        ConfigError.combined(missingKey, missingKey)
          .isMissingKey shouldBe true
      }

      "return false for a combination of not only MissingKeys" in {
        ConfigError.combined(missingKey, ConfigError("other"))
          .isMissingKey shouldBe false
      }

      "return false for other errors" in {
        ConfigError("other").isMissingKey shouldBe false
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
