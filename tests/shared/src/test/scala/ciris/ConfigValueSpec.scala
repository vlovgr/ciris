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
  }
}
