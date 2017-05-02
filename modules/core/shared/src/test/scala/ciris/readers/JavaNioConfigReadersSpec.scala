package ciris.readers

import java.nio.file.Path

import ciris.PropertySpec

final class JavaNioConfigReadersSpec extends PropertySpec {
  "JavaNioConfigReaders" when {
    "reading a Path" should {
      "successfully read Path values" in {
        forAll { string: String ⇒
          readValue[Path](string) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
