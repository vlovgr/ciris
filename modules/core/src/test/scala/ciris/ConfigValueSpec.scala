package ciris

import cats.Id
import cats.implicits._
import ciris.ConfigError.right

final class ConfigValueSpec extends PropertySpec {

  "ConfigValue" when {
    "created with apply" should {
      "be able to retrieve the value" in {
        ConfigValue(Right(123)).value shouldBe Right(123)
      }

      "include the result in toStringWithResult" in {
        ConfigValue(Right(123)).toStringWithResult shouldBe "ConfigValue(Right(123))"
      }

      "include the value in toStringWithValue" in {
        ConfigValue(Right(123)).toStringWithValue shouldBe "ConfigValue(Right(123))"
      }

      "not include the value in toString" in {
        ConfigValue(Right(123)).toString shouldNot be("ConfigValue(123)")
      }

      "not include the value in show" in {
        ConfigValue(Right(123)).show shouldNot be("ConfigValue(123)")
      }
    }

    "created with applyF" should {
      "be able to retrieve the value" in {
        ConfigValue.applyF[Id, Int](right(123)).value shouldBe Right(123)
      }

      "include the value in toStringWithValue" in {
        ConfigValue.applyF[Id, Int](right(123)).toStringWithValue shouldBe "ConfigValue(Right(123))"
      }

      "not include the value in toString" in {
        ConfigValue.applyF[Id, Int](right(123)).toString shouldNot be("ConfigValue(123)")
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
        readNonExistingConfigEntry[String].orNone.value shouldBe Right(None)
      }

      "use None for combined missing keys" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry)
          .orNone
          .value shouldBe Right(None)
      }

      "keep an error that is not a missing key" in {
        ConfigValue(ConfigError.left[String](ConfigError("error"))).orNone.value.left
          .map(_.message) shouldBe Left("error")
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

    "using orValue" should {
      "use value for a missing key" in {
        readNonExistingConfigEntry[String]
          .orValue("value")
          .value shouldBe Right("value")
      }

      "use value for combined missing keys" in {
        readNonExistingConfigEntry[String]
          .orElse(readNonExistingConfigEntry)
          .orValue("value")
          .value shouldBe Right("value")
      }

      "keep an error that is not a missing key" in {
        ConfigValue(ConfigError.left[String](ConfigError("error")))
          .orValue("value")
          .value
          .left
          .map(_.message) shouldBe Left("error")
      }

      "keep a combined error that is not only missing keys" in {
        readNonExistingConfigEntry[String]
          .orElse(ConfigValue(ConfigError.left(ConfigError("error"))))
          .orValue("value")
          .value
          .left
          .map(_.message) shouldBe Left("Missing test key [key] and error")
      }
    }
  }
}
