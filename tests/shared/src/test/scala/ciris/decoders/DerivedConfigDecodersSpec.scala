package ciris.decoders

import ciris.PropertySpec

final class DerivedConfigDecodersSpec extends PropertySpec {
  "DerivedConfigDecoders" when {
    "reading an Option" should {
      "successfully read existing values" in {
        forAll { string: String =>
          readValue[Option[String]](string) shouldBe Right(Some(string))
        }
      }

      "return a None for non-existing value" in {
        readNonExistingValue[Option[String]] shouldBe Right(None)
      }
    }
  }
}
