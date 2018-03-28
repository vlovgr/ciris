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
        readConfigEntry[String]("value")
          .orElse(fail("evaluated alternative"))
      }

      "not evaluate the alternative when this value is not a missing key error" in {
        ConfigValue(ConfigError.left[String](ConfigError("error")))
          .orElse(fail("evaluated alternative"))
      }

      "use the alternative value if the first key is missing" in {
        readNonExistingConfigEntry[String]
          .orElse(readConfigEntry("value"))
          .value shouldBe Right("value")
      }

      "use the alternative value if the first keys are missing" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry)
          .orElse(readConfigEntry("value"))
          .value shouldBe Right("value")
      }

      "accumulate errors if both values are unavailable" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry)
          .value
          .left
          .map(_.message) shouldBe Left("Missing test key [key] and missing test key [key]")
      }
    }

    "using orNone" should {
      "use None for a missing key" in {
        readNonExistingConfigEntry[String]
          .orNone
          .value shouldBe Right(None)
      }

      "use None for combined missing keys" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry)
          .orNone
          .value shouldBe Right(None)
      }

      "keep an error that is not a missing key" in {
        ConfigValue(ConfigError.left[String](ConfigError("error")))
          .orNone
          .value
          .left.map(_.message) shouldBe Left("error")
      }

      "keep a combined error that is not only missing keys" in {
        readNonExistingConfigEntry[String]
          .orElse(ConfigValue(ConfigError.left(ConfigError("error"))))
          .orNone
          .value
          .left
          .map(_.message) shouldBe Left("Missing test key [key] and error")
      }

      "wrap available values in Some" in {
        nonExistingEntry
          .orElse(existingEntry("value"))
          .orNone
          .value shouldBe Right(Some("value"))
      }
    }
  }
}
