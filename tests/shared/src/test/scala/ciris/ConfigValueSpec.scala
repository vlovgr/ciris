package ciris

import ciris.ConfigError.right
import ciris.api._

final class ConfigValueSpec extends PropertySpec {

  "ConfigValue" when {
    "created with apply" should {
      "be able to retrieve the value" in {
        ConfigValue(Right(123)).value shouldBe Right(123)
      }

      "have the expected string representation" in {
        ConfigValue(Right(123)).toString shouldBe "ConfigValue(Right(123))"
      }
    }

    "created with applyF" should {
      "be able to retrieve the value" in {
        ConfigValue.applyF[Id, Int](right(123)).value shouldBe Right(123)
      }

      "have the expected string representation" in {
        ConfigValue.applyF[Id, Int](right(123)).toString shouldBe "ConfigValue(Right(123))"
      }
    }

    "using orElse" should {
      "not evaluate the alternative when this value is available" in {
        var read = false

        readConfigEntry[String]("value")
          .orElse({
            read = true
            readConfigEntry[String]("value2")
          })

        withClue("alternative was evaluated although it should not:") {
          read shouldBe false
        }
      }

      "use the alternative if it is available, when this value is not" in {
        readNonExistingConfigEntry[String]
          .orElse(readConfigEntry[String]("value"))
          .value shouldBe Right("value")
      }

      "accumulate errors if both values are unavailable" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry[String])
          .value
          .left
          .map(_.message) shouldBe Left("Missing test key [key] and missing test key [key]")
      }
    }

    "using orNone" should {
      "wrap existing values in Some" in {
        nonExistingEntry
          .orElse(existingEntry("value"))
          .orNone
          .value shouldBe Right(Some("value"))
      }

      "discard errors and return None" in {
        nonExistingEntry
          .orElse(nonExistingEntry)
          .orNone
          .value shouldBe Right(None)
      }
    }
  }
}
