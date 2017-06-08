package ciris

final class ConfigReaderSpec extends PropertySpec {
  "ConfigReader" when {
    "using mapBoth" should {
      val reader = ConfigReader.mapBoth(_ => Right("error"), value => Right(value + "123"))

      "map the error if there is one" in {
        reader.read(nonExistingEntry) shouldBe Right("error")
      }

      "map the value if there is one" in {
        forAll { value: String =>
          reader.read(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }

    "using map" should {
      val reader = ConfigReader.map(value => Right(value + "123"))

      "keep the error if there is one" in {
        reader.read(nonExistingEntry) shouldBe a[Left[_, _]]
      }

      "map the value if there is one" in {
        forAll { value: String =>
          reader.read(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }
  }
}
