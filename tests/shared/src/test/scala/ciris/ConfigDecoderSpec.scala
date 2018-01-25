package ciris

import scala.util.Try

final class ConfigDecoderSpec extends PropertySpec {
  "ConfigDecoder" when {
    "using mapBoth" should {
      val decoder = ConfigDecoder.mapBoth[String](_ => Right("error"), value => Right(value + "123"))

      "map the error if there is one" in {
        decoder.decode(nonExistingEntry) shouldBe Right("error")
      }

      "map the value if there is one" in {
        forAll { value: String =>
          decoder.decode(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }

    "using fromTryOption[A]" should {
      val decoder = ConfigDecoder.fromTryOption[String]("typeName")(value => Try {
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

    "using fromTryOption[A, B]" should {
      val decoder = ConfigDecoder.fromTryOption[String, String]("typeName")(value => Try {
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

    "using fromTry[A, B]" should {
      val decoder = ConfigDecoder.fromTry[String, String]("typeName")(value => Try {
        value match {
          case "success" => "ok"
          case _ => throw new Error
        }
      })

      "succeed for Success" in {
        decoder.decode(existingEntry("success")) shouldBe a[Right[_, _]]
      }

      "fail for Failure" in {
        decoder.decode(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using fromOption[A, B]" should {
      val decoder = ConfigDecoder.fromOption[String, String]("typeName") {
        case "some" => Some("ok")
        case _ => None
      }

      "succeed for Some" in {
        decoder.decode(existingEntry("some")) shouldBe a[Right[_, _]]
      }

      "fail for None" in {
        decoder.decode(existingEntry("none")) shouldBe a[Left[_, _]]
      }
    }

    "using mapTry" should {
      val decoder = ConfigDecoder.identity[String].mapTry("typeName")(value => Try {
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

      "fail if the entry does not exist" in {
        decoder.decode(nonExistingEntry) shouldBe a[Left[_, _]]
      }
    }

    "using mapCatchNonFatal" should {
      val decoder = ConfigDecoder.identity[String].mapCatchNonFatal("typeName") {
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

    "using catchNonFatal" should {
      val decoder = ConfigDecoder.catchNonFatal[String, String]("typeName") {
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
      val decoder = ConfigDecoder.identity[String].collect("typeName") {
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

    "using map[A]" should {
      val decoder = ConfigDecoder.map[String](value => Right(value + "123"))

      "keep the error if there is one" in {
        decoder.decode(nonExistingEntry) shouldBe a[Left[_, _]]
      }

      "map the value if there is one" in {
        forAll { value: String =>
          decoder.decode(existingEntry(value)) shouldBe Right(value + "123")
        }
      }
    }

    "using map[A, B]" should {
      val decoder = ConfigDecoder.map[String, String](value => Right(value + "123"))

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
      val decoder = ConfigDecoder[String].mapEntryValue(f)

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
