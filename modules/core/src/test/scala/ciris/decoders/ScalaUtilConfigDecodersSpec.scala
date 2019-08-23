package ciris.decoders

import scala.util.matching.Regex

import ciris.PropertySpec
import org.scalacheck.Gen

final class ScalaUtilConfigDecodersSpec extends PropertySpec {
  "ScalaUtilConfigDecoders" when {
    "reading a Regex" should {
      "successfully read Regex values" in {
        val examplePatterns = List("[a-z&&[def]]", "(\\D)")
        forAll(Gen.oneOf(examplePatterns)) { examplePattern =>
          readValue[Regex](examplePattern) shouldBe a[Right[_, _]]
        }
      }

      "return a failure for other values" in {
        val exampleInvalidPatterns = List("[AB", "(CD[E]")
        forAll(Gen.oneOf(exampleInvalidPatterns)) { exampleInvalidPattern =>
          readValue[Regex](exampleInvalidPattern) shouldBe a[Left[_, _]]
        }
      }
    }
  }
}
