package ciris.decoders

import java.nio.charset.Charset

import ciris.PropertySpec

final class JavaNioCharsetConfigDecodersSpec extends PropertySpec {
  "JavaNioCharsetConfigDecoders" when {
    "reading a Charset" should {
      "return a failure for unrecognized values" in {
        forAll { string: String =>
          whenever(fails(Charset.forName(string))) {
            readValue[Charset](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
