package ciris.decoders

import java.net.{InetAddress, URI, URL}

import ciris.PropertySpec
import org.scalacheck.Gen

final class JavaNetConfigDecodersSpec extends PropertySpec {
  "JavaNetConfigDecoders" when {
    "reading an InetAddress" should {
      "successfully read InetAddress values" in {
        val exampleAddresses = Gen.oneOf("localhost", "127.0.0.1")
        forAll(exampleAddresses) { exampleAddress =>
          readValue[InetAddress](exampleAddress) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading an URI" should {
      "successfully read URI values" in {
        val exampleUris = Gen.oneOf("http://localhost", "ftp://localhost")
        forAll(exampleUris) { exampleUri: String =>
          readValue[URI](exampleUri) shouldBe a[Right[_, _]]
        }
      }
    }

    "reading an URL" should {
      "successfully read URL values" in {
        val exampleUrls = Gen.oneOf("http://localhost", "ftp://localhost")
        forAll(exampleUrls) { exampleUrl: String =>
          readValue[URL](exampleUrl) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
