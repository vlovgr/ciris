package ciris

import scala.util.Try

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

    "using fromTryOption" should {
      val reader = ConfigReader.fromTryOption("typeName")(value => Try {
        value match {
          case "some" => Some("ok")
          case "none" => None
          case _ => throw new Error
        }
      })

      "succeed for Success[Some]" in {
        reader.read(existingEntry("some")) shouldBe a[Right[_, _]]
      }

      "fail for Success[None]" in {
        reader.read(existingEntry("none")) shouldBe a[Left[_, _]]
      }

      "fail for Failure" in {
        reader.read(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using mapTry" should {
      val reader = ConfigReader.identity.mapTry("typeName")(value => Try {
        value match {
          case "ok" => "ok"
          case _ => throw new Error
        }
      })

      "succeed for Success" in {
        reader.read(existingEntry("ok")) shouldBe a[Right[_, _]]
      }

      "fail for Failure" in {
        reader.read(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using mapCatchNonFatal" should {
      val reader = ConfigReader.identity.mapCatchNonFatal("typeName") {
        case "ok" => "ok"
        case _ => throw new Error
      }

      "succeed for Success" in {
        reader.read(existingEntry("ok")) shouldBe a[Right[_, _]]
      }

      "fail for Failure" in {
        reader.read(existingEntry("error")) shouldBe a[Left[_, _]]
      }
    }

    "using collect" should {
      val reader = ConfigReader.identity.collect("typeName") {
        case value if value == "ok" =>
          "ok"
      }

      "succeed if the partial function is defined" in {
        reader.read(existingEntry("ok")) shouldBe a [Right[_, _]]
      }

      "fail if the partial function is undefined" in {
        reader.read(existingEntry("error")) shouldBe a [Left[_, _]]
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
