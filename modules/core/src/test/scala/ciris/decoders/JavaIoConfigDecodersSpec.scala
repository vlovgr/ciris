package ciris.decoders

import java.io.File

import ciris.PropertySpec

final class JavaIoConfigDecodersSpec extends PropertySpec {
  "JavaIoConfigDecoders" when {
    "reading a File" should {
      "successfully read File values" in {
        forAll { string: String =>
          readValue[File](string) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
