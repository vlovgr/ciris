package ciris

import ciris.api._

final class ConfigEntrySpec extends PropertySpec {
  "ConfigEntry" when {
    "converting to String" should {
      "include the key, keyType, and value" in {
        forAll { value: String =>
          existingEntry(value).toString shouldBe
            s"ConfigEntry(key, ConfigKeyType(test key), Right($value))"
        }
      }

      "include the sourceValue as well if different from the value" in {
        forAll { value: String =>
          existingEntry(value)
            .mapValue(_ + "2")
            .toString shouldBe {
            s"ConfigEntry(key, ConfigKeyType(test key), Right($value), Right(${value}2))"
          }
        }
      }
    }

    "using withValue" should {
      "replace the existing value" in {
        existingEntry("value").withValue(Right("value2")).value shouldBe Right("value2")
      }
    }

    "using flatMapValue" should {
      "keep this error" in {
        nonExistingEntry.flatMapValue(_ => Right("value")).value shouldBe a[Left[_, _]]
      }

      "replace the value" in {
        existingEntry("value").flatMapValue(_ => Right("value2")).value shouldBe Right("value2")
      }
    }

    "using mapValue" when {
      "the value was read successfully" should {
        "apply the function on the value" in {
          forAll { value: String =>
            val f: String => String = _.take(1)
            val entry = existingEntry(value).mapValue(f)
            entry.value shouldBe Right(f(value))
          }
        }
      }

      "the value was not read successfully" should {
        "leave the value as it is" in {
          val entry = nonExistingEntry.mapValue(_.take(1))
          entry.value shouldBe nonExistingEntry.value
        }
      }
    }

    "using liftF" when {
      "transforming Id to Id" should {
        "leave the values unmodified" in {
          val entry = existingEntry("value")
          val lifted = entry.liftF[Id]

          lifted.value shouldBe entry.value
          lifted.sourceValue shouldBe entry.sourceValue
        }
      }
    }

    "using orElse" when {
      "this value was read successfully" should {
        "use this value and not the other one" in {
          existingEntry("value")
            .orElse(existingEntry("value2"))
            .value shouldBe Right("value")
        }
      }

      "this value was not read successfully" when {
        "the other value was read successfully" should {
          "use the other value" in {
            nonExistingEntry
              .orElse(existingEntry("value2"))
              .value shouldBe Right("value2")
          }
        }

        "the other value was not read successfully" should {
          "accumulate the errors of both values" in {
            val (first, second) = (nonExistingEntry, nonExistingEntry)
            val (error1, error2) = (first.value.left.get, second.value.left.get)
            first.orElse(second).value.left.get.message shouldBe (error1 combine error2).message
          }
        }
      }
    }
  }
}
