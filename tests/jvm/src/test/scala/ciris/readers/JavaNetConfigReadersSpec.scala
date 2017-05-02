package ciris.readers

import java.net.{URI, URL}

import ciris.PropertySpec
import org.scalacheck.Gen

final class JavaNetConfigReadersSpec extends PropertySpec {
  "JavaNetConfigReaders" when {
    "reading an URI" should {
      "successfully read URI values" in {
        val exampleUris = Gen.oneOf("http://localhost", "ftp://localhost")
        forAll(exampleUris) { exampleUri: String ⇒
          readValue[URI](exampleUri) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading an URL" should {
      "successfully read URL values" in {
        val exampleUrls = Gen.oneOf("http://localhost", "ftp://localhost")
        forAll(exampleUrls) { exampleUrl: String ⇒
          readValue[URL](exampleUrl) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
