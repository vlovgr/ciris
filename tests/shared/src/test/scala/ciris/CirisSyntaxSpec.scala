package ciris

import ciris.syntax._

final class CirisSyntaxSpec extends PropertySpec {
  "Ciris syntax" when {
    "using orThrow" should {
      "return the configuration if loaded successfully" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readConfigEntry[String]("key2")
        )(_ + _)

        noException shouldBe thrownBy {
          config.orThrow()
        }
      }

      "throw an exception if loading failed" in {
        val config = loadConfig(
          readConfigEntry[String]("key1"),
          readNonExistingConfigEntry[String]
        )(_ + _)

        a[ConfigException] shouldBe thrownBy {
          config.orThrow()
        }
      }
    }
  }
}
