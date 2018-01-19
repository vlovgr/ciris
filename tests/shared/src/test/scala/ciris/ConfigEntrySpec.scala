package ciris

final class ConfigEntrySpec extends PropertySpec {
  "ConfigEntry" when {
    "converting to String" should {
      "include the key, keyType, and value" in {
        forAll { value: String =>
          existingEntry(value).toString shouldBe
            s"ConfigEntry(key, ConfigKeyType(test key), Right($value))"
        }
      }
    }

    "using mapValue" when {
      "the value was read successfully" should {
        "apply the function on the value" in {
          forAll { value: String =>
            val f: String => String = _.take(1)
            val entry = existingEntry(value).mapValueRight(f)
            entry.value shouldBe Right(f(value))
          }
        }
      }

      "the value was not read successfully" should {
        "leave the value as it is" in {
          val entry = nonExistingEntry.mapValueRight(_.take(1))
          entry.value shouldBe nonExistingEntry.value
        }
      }
    }
  }
}
