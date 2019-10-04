package ciris

import cats.implicits._

final class ConfigKeyTypeSpec extends PropertySpec {
  "ConfigKeyType" when {
    "converting to String" should {
      "include the value" in {
        forAll { value: String =>
          val keyType = ConfigKeyType(value)
          keyType.toString shouldBe s"ConfigKeyType($value)"
          keyType.show shouldBe keyType.toString

        }
      }
    }

    "converting Argument to String" should {
      "be Argument" in {
        ConfigKeyType.Argument.toString shouldBe "Argument"
      }
    }

    "converting Environment to String" should {
      "be Environment" in {
        ConfigKeyType.Environment.toString shouldBe "Environment"
      }
    }

    "converting Property to String" should {
      "be Property" in {
        ConfigKeyType.Property.toString shouldBe "Property"
      }
    }
  }
}
