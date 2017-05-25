package ciris.readers

import java.util.UUID
import java.util.regex.Pattern

import ciris.PropertySpec
import org.scalacheck.Gen

final class JavaUtilConfigReadersSpec extends PropertySpec {
  "JavaUtilConfigReaders" when {
    "reading a regex Pattern" should {
      "successfully read Pattern values" in {
        val examplePatterns = List("[a-z&&[def]]", "(\\D)")
        forAll(Gen.oneOf(examplePatterns)) { examplePattern =>
          readValue[Pattern](examplePattern) shouldBe a[Right[_, _]]
        }
      }

      "return a failure for other values" in {
        val exampleInvalidPatterns = List("[AB", "(CD[E]")
        forAll(Gen.oneOf(exampleInvalidPatterns)) { exampleInvalidPattern =>
          readValue[Pattern](exampleInvalidPattern) shouldBe a[Left[_, _]]
        }
      }
    }

    "reading an UUID" should {
      "successfully read UUID values" in {
        forAll(Gen.uuid) { uuid =>
          readValue[UUID](uuid.toString) shouldBe Right(uuid)
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(UUID.fromString(string))) {
            readValue[UUID](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
