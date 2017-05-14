package ciris.readers

import java.nio.file.{Path, Paths}

import ciris.PropertySpec
import org.scalacheck.Gen

final class JavaNioFileConfigReadersSpec extends PropertySpec {
  "JavaNioFileConfigReaders" when {
    "reading a Path" should {
      "successfully read Path values" in {
        forAll { string: String ⇒
          whenever(!fails(Paths.get(string))) {
            readValue[Path](string) shouldBe Right(Paths.get(string))
          }
        }
      }

      "return a failure for invalid values" in {
        val invalidPaths = List[String](null, "\u0000")
        forAll(Gen.oneOf(invalidPaths)) { invalidPath ⇒
          readValue[Path](invalidPath) shouldBe a[Left[_, _]]
        }
      }
    }
  }
}
