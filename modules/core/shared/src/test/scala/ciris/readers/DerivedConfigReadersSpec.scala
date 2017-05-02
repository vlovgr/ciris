package ciris.readers

import ciris.PropertySpec

final class DerivedConfigReadersSpec extends PropertySpec {
  "DerivedConfigReaders" when {
    "reading an Option" should {
      "successfully read existing values" in {
        forAll { string: String ⇒
          read[Option[String]](string) shouldBe Right(Some(string))
        }
      }

      "return a None for non-existing value" in {
        readEmpty[Option[String]] shouldBe Right(None)
      }
    }
  }
}
