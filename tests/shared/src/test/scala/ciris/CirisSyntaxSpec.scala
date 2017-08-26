package ciris

import ciris.syntax._

final class CirisSyntaxSpec extends PropertySpec {
  "Ciris syntax" when {
    "using orThrow" should {
      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigValue[String]("key1"),
          readConfigValue[String]("key2")
        )(_ + _)

        noException shouldBe thrownBy {
          config.orThrow()
        }
      }

      "throw an exception if loading failed" in {
        val config = loadConfig(
          readConfigValue[String]("key1"),
          readNonExistingConfigValue[String]
        )(_ + _)

        a[ConfigException] shouldBe thrownBy {
          config.orThrow()
        }
      }
    }
  }
}
