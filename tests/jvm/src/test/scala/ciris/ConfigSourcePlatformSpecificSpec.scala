package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

final class ConfigSourcePlatformSpecificSpec extends JvmPropertySpec {
  "ConfigSourcePlatformSpecific" when {
    "reading from a file" when {
      "the file exists" should {
        "return the file contents" in {
          forAll { value: String =>
            withFile(value) { (file, charset) =>
              ConfigSource.File.read((file, charset)).value shouldBe Right(value)
            }
          }
        }
      }

      "the file does not exist" should {
        "return an error" in {
          val key = (new JFile("/tmp/does-not-exist"), Charset.defaultCharset)
          ConfigSource.File.read(key).value shouldBe a[Left[_, _]]
        }
      }
    }
  }

}
