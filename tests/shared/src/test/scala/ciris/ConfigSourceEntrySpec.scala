package ciris

final class ConfigSourceEntrySpec extends PropertySpec {
  "ConfigSourceEntry" when {
    "converting to String" should {
      "include the key, keyType, and value" in {
        forAll { value: String =>
          existingEntry(value).toString shouldBe
            s"ConfigSourceEntry(key, ConfigKeyType(test key), Right($value))"
        }
      }
    }
  }
}
