package ciris

final class ConfigValueSpec extends PropertySpec {
  "ConfigValue" when {
    "converting to String" should {
      "include the value" in {
        readConfigValue[String]("value").toString shouldBe "ConfigValue(Right(value))"
      }
    }
  }
}
