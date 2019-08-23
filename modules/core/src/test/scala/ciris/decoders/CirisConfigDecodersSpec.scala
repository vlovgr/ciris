package ciris.decoders

import ciris.{ConfigDecoder, PropertySpec, Secret}

final class CirisConfigDecodersSpec extends PropertySpec {
  "CirisConfigDecodersSpec" when {
    "decoding a Secret" should {
      "redact error messages" in {
        val decoded = ConfigDecoder[String, Secret[Int]].decode(existingEntry("abc"))
        decoded.left.map(_.message) shouldBe Left("Test key [key] with value [<redacted>] cannot be converted to type [Int]")

      }
    }
  }
}
