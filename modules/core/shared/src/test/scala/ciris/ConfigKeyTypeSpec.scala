package ciris

final class ConfigKeyTypeSpec extends PropertySpec {
  "ConfigKeyType" when {
    "converting to String" should {
      "include the value" in {
        forAll { value: String ⇒
          ConfigKeyType(value).toString shouldBe s"ConfigKeyType($value)"
        }
      }
    }
  }
}
