package ciris

import scala.util.Try

final class ConfigDecoderSpec extends PropertySpec {
  "ConfigDecoder" when {
    "using mapBoth" should {
      val decoder = ConfigDecoder.mapBoth(_ => Right("error"), value => Right(value + "123"))

      "map the error if there is one" in {
        decoder.decode(nonExistingEntry) shouldBe Right("error")
      }

      "map the value if there is one" in {
        forAll { value: String =>
          decoder.decode(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }

    "using fromTryOption" should {
      val decoder = ConfigDecoder.fromTryOption("typeName")(value => Try {
        value match {
          case "some" => Some("ok")
          case "none" => None
          case _ => throw new Error
        }
      })

      "succeed for Success[Some]" in {
        decoder.decode(existingEntry("some")) shouldBe a[Right[_, _]]
      }

      "fail for Success[None]" in {
        decoder.decode(existingEntry("none")) shouldBe a[Left[_, _]]
      }

      "fail for Failure" in {
        decoder.decode(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using mapTry" should {
      val decoder = ConfigDecoder.identity.mapTry("typeName")(value => Try {
        value match {
          case "ok" => "ok"
          case _ => throw new Error
        }
      })

      "succeed for Success" in {
        decoder.decode(existingEntry("ok")) shouldBe a[Right[_, _]]
      }

      "fail for Failure" in {
        decoder.decode(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using mapCatchNonFatal" should {
      val decoder = ConfigDecoder.identity.mapCatchNonFatal("typeName") {
        case "ok" => "ok"
        case _ => throw new Error
      }

      "succeed for Success" in {
        decoder.decode(existingEntry("ok")) shouldBe a[Right[_, _]]
      }

      "fail for Failure" in {
        decoder.decode(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using collect" should {
      val decoder = ConfigDecoder.identity.collect("typeName") {
        case value if value == "ok" =>
          "ok"
      }

      "succeed if the partial function is defined" in {
        decoder.decode(existingEntry("ok")) shouldBe a [Right[_, _]]
      }

      "fail if the partial function is undefined" in {
        decoder.decode(existingEntry("error")) shouldBe a [Left[_, _]]
      }
    }

    "using map" should {
      val decoder = ConfigDecoder.map(value => Right(value + "123"))

      "keep the error if there is one" in {
        decoder.decode(nonExistingEntry) shouldBe a[Left[_, _]]
      }

      "map the value if there is one" in {
        forAll { value: String =>
          decoder.decode(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }

    "using mapEntryValue" should {
      val f: String => String = _.take(1)
      val decoder = ConfigDecoder.identity.mapEntryValue(f)

      "keep the error if there is one" in {
        decoder.decode(nonExistingEntry) shouldBe a[Left[_, _]]
      }

      "map the entry value if there is one" in {
        forAll { value: String =>
          decoder.decode(existingEntry(value)) shouldBe Right(f(value))
        }
      }
    }
  }
}
