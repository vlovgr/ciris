package ciris.readers

import java.io.File

import ciris.PropertySpec

final class JavaIoConfigReadersSpec extends PropertySpec {
  "JavaIoConfigReaders" when {
    "reading a File" should {
      "successfully read File values" in {
        forAll { string: String =>
          readValue[File](string) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
