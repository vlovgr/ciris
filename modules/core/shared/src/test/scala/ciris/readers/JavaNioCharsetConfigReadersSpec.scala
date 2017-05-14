package ciris.readers

import java.nio.charset.Charset

import ciris.PropertySpec

final class JavaNioCharsetConfigReadersSpec extends PropertySpec {
  "JavaNioCharsetConfigReaders" when {
    "reading a Charset" should {
      "return a failure for unrecognized values" in {
        forAll { string: String ⇒
          whenever(fails(Charset.forName(string))) {
            readValue[Charset](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
