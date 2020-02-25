package ciris

import cats.kernel.laws.discipline.EqTests

final class ConfigExceptionSpec extends BaseSpec {
  checkAll("ConfigException", EqTests[ConfigException].eqv)

  test("ConfigException.error") {
    forAll { error: ConfigError => assert(ConfigException(error).error === error) }
  }

  test("ConfigException.message.contains") {
    forAll { exception: ConfigException =>
      val exceptionMessage =
        exception.getMessage

      val messages =
        exception.error.messages

      val withEntryTrailing =
        messages
          .filter(_.endsWith(ConfigException.entryTrailing))
          .forall { message =>
            exceptionMessage.contains {
              s"${ConfigException.entryLeading}$message"
            }
          }

      val withoutEntryTrailing =
        messages
          .filterNot(_.endsWith(ConfigException.entryTrailing))
          .forall { message =>
            exceptionMessage.contains {
              s"${ConfigException.entryLeading}$message${ConfigException.entryTrailing}"
            }
          }

      assert(withEntryTrailing && withoutEntryTrailing)
    }
  }

  test("ConfigException.message.leading") {
    forAll { exception: ConfigException =>
      assert(exception.getMessage.startsWith(ConfigException.messageLeading))
    }
  }

  test("ConfigException.message.trailing") {
    forAll { exception: ConfigException =>
      assert(exception.getMessage.endsWith(ConfigException.messageTrailing))
    }
  }

  test("ConfigException.messageLength") {
    forAll { exception: ConfigException =>
      val expected = ConfigException.messageLength(exception.error.messages)
      val actual = exception.getMessage.length
      assert(actual === expected)
    }
  }

  test("ConfigException.toString") {
    forAll { exception: ConfigException =>
      assert(exception.toString === s"ciris.ConfigException: ${exception.getMessage}")
    }
  }

  test("ConfigException.unapply") {
    forAll { exception: ConfigException =>
      assert {
        exception match {
          case ConfigException(error) =>
            error === exception.error
        }
      }
    }
  }
}
