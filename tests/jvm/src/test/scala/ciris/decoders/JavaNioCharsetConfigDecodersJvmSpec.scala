package ciris.decoders

import java.nio.charset.Charset

import ciris.PropertySpec
import org.scalacheck.Gen

import scala.collection.JavaConverters._

final class JavaNioCharsetConfigDecodersJvmSpec extends PropertySpec {
  "JavaNioCharsetConfigDecoders" when {
    "reading a Charset" should {
      "successfully read available charsets" in {
        val availableCharsets = Charset.availableCharsets().keySet().asScala.toSeq
        val genAvailableCharset = Gen.oneOf(availableCharsets).flatMap(mixedCase)

        forAll(genAvailableCharset) { charset =>
          readValue[Charset](charset) shouldBe a[Right[_, _]]
        }
      }
    }
  }
}
