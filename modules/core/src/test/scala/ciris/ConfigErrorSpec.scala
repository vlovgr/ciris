package ciris

import cats.implicits._
import cats.kernel.laws.discipline.EqTests
import cats.data.Chain
import org.scalacheck.Gen

final class ConfigErrorSpec extends BaseSpec {
  test("ConfigError.and.messages") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.And(errors).messages
      assert(messages.size == errors.foldMap(_.messages.size))

      assert {
        errors.forall(_.messages.forall { m => messages.exists(_.contains(m)) })
      }
    }
  }

  test("ConfigError.and.redacted") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.And(errors).redacted.messages
      assert(messages.size == errors.foldMap(_.messages.size))

      assert {
        errors
          .map(_.redacted)
          .forall(_.messages.forall { m => messages.exists(_.contains(m)) })
      }
    }
  }

  test("ConfigError.and.toString") {
    forAll { (errors: Chain[ConfigError]) =>
      val error = ConfigError.And(errors)
      assert(error.toString === s"And(${errors.toList.mkString(", ")})")
    }
  }

  test("ConfigError.apply.messages") {
    forAll { (message: String) =>
      val messages = ConfigError(message).messages
      assert(messages.size == 1)

      val head = messages.toList.head
      assert(head === message)
    }
  }

  test("ConfigError.apply.redacted") {
    forAll { (message: String) =>
      val messages = ConfigError(message).redacted.messages
      assert(messages.size == 1)

      val head = messages.toList.head
      assert(head === message)
    }
  }

  test("ConfigError.apply.hashCode") {
    forAll { (message: String, message2: String) =>
      whenever(message !== message2) {
        val error = ConfigError(message)
        val error2 = ConfigError(message2)
        val error3 = ConfigError(message)

        assert {
          (error.hashCode !== error2.hashCode) &&
          (error2.hashCode !== error3.hashCode) &&
          (error.hashCode === error3.hashCode)
        }
      }
    }
  }

  test("ConfigError.apply.equals") {
    forAll { (message: String, message2: String) =>
      whenever(message !== message2) {
        val error = ConfigError(message)
        val error2 = ConfigError(message2)
        val error3 = ConfigError(message)

        assert {
          (error != error2) &&
          (error2 != error3) &&
          (error == error3)
        }
      }
    }
  }

  test("ConfigError.apply.toString") {
    forAll { (message: String) =>
      val error = ConfigError(message)
      assert(error.toString === s"ConfigError($message)")
    }
  }

  test("ConfigError.decode.key") {
    forAll { (typeName: String, key: ConfigKey, value: String) =>
      val error = ConfigError.decode(typeName, Some(key), value)
      assert(
        error.messages === Chain
          .one(s"${key.description.capitalize} with value $value cannot be converted to $typeName")
      )
    }
  }

  test("ConfigError.decode.key redacted") {
    forAll { (typeName: String, key: ConfigKey, value: String) =>
      val error = ConfigError.decode(typeName, Some(key), value)
      assert(
        error.redacted.messages === Chain
          .one(s"${key.description.capitalize} cannot be converted to $typeName")
      )
    }
  }

  test("ConfigError.decode.no key") {
    forAll { (typeName: String, value: String) =>
      val error = ConfigError.decode(typeName, None, value)
      assert(error.messages === Chain.one(s"Unable to convert value $value to $typeName"))
    }
  }

  test("ConfigError.decode.no key redacted") {
    forAll { (typeName: String, value: String) =>
      val error = ConfigError.decode(typeName, None, value)
      assert(error.redacted.messages === Chain.one(s"Unable to convert value to $typeName"))
    }
  }

  test("ConfigError.empty.messages") {
    assert(ConfigError.Empty.messages.isEmpty)
  }

  test("ConfigError.empty.redacted") {
    assert(ConfigError.Empty.redacted === ConfigError.Empty)
  }

  test("ConfigError.empty.toString") {
    assert(ConfigError.Empty.toString === "Empty")
  }

  checkAll("ConfigError", EqTests[ConfigError].eqv)

  test("ConfigError.loaded.messages") {
    assert(ConfigError.Loaded.messages.isEmpty)
  }

  test("ConfigError.loaded.redacted") {
    assert(ConfigError.Loaded.redacted === ConfigError.Loaded)
  }

  test("ConfigError.loaded.toString") {
    assert(ConfigError.Loaded.toString === "Loaded")
  }

  test("ConfigError.missing.messages") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      assert(error.messages.size == 1)

      val message = error.messages.toList.head
      assert(message === s"Missing ${ConfigError.uncapitalize(key.description)}")
    }
  }

  test("ConfigError.missing.redacted") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      assert(error.redacted === error)
    }
  }

  test("ConfigError.missing.toString") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      assert(error.toString === s"Missing($key)")
    }
  }

  test("ConfigError.normalize") {
    forAll { (errors: Chain[ConfigError]) =>
      val actual =
        ConfigError.normalize(errors, ConfigError.And)

      val expected =
        if (errors.isEmpty)
          ConfigError.And(errors)
        else if (errors.forall(_ === ConfigError.Loaded))
          ConfigError.Loaded
        else if (errors.contains(ConfigError.Loaded))
          ConfigError.And(errors.filterNot(_ === ConfigError.Loaded).prepend(ConfigError.Loaded))
        else
          ConfigError.And(errors)

      assert(actual === expected)
    }
  }

  test("ConfigError.or.messages") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.Or(errors).messages
      assert(messages.size == 1)
    }
  }

  test("ConfigError.or.redacted") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.Or(errors).redacted.messages
      assert(messages.size == 1)

      val message = messages.toList.head.toLowerCase
      assert {
        errors
          .map(_.redacted)
          .forall(_.messages.forall { m => message.contains(m.toLowerCase) })
      }
    }
  }

  test("ConfigError.or.toString") {
    forAll { (errors: Chain[ConfigError]) =>
      val error = ConfigError.Or(errors)
      assert(error.toString === s"Or(${errors.toList.mkString(", ")})")
    }
  }

  test("ConfigError.sensitive.messages") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      assert(error.messages === Chain.one(message))
    }
  }

  test("ConfigError.sensitive.redacted") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      assert(error.redacted.messages === Chain.one(redactedMessage))
    }
  }

  test("ConfigError.sensitive.hashCode") {
    forAll { (message: String, redactedMessage: String) =>
      whenever(message !== redactedMessage) {
        val error = ConfigError.sensitive(message, redactedMessage)
        val error2 = ConfigError.sensitive(message, message)
        val error3 = ConfigError.sensitive(redactedMessage, redactedMessage)

        assert {
          (error.hashCode !== error2.hashCode) &&
          (error2.hashCode !== error3.hashCode) &&
          (error.hashCode !== error3.hashCode)
        }
      }
    }
  }

  test("ConfigError.sensitive.equals.sensitive") {
    forAll { (message: String, redactedMessage: String) =>
      whenever(message !== redactedMessage) {
        val error = ConfigError.sensitive(message, redactedMessage)
        val error2 = ConfigError.sensitive(message, message)
        val error3 = ConfigError.sensitive(redactedMessage, redactedMessage)
        val error4 = ConfigError.sensitive(message, redactedMessage)

        assert {
          (error != error2) &&
          (error2 != error3) &&
          (error != error3) &&
          (error == error4)
        }
      }
    }
  }

  test("ConfigError.sensitive.equals.non sensitive") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      assert {
        (error: Any) != message &&
        (error: Any) != redactedMessage
      }
    }
  }

  test("ConfigError.sensitive.toString") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      assert(error.toString === s"Sensitive($message, $redactedMessage)")
    }
  }

  test("ConfigError#throwable") {
    forAll { (error: ConfigError) =>
      assert {
        error.throwable match {
          case ConfigException(e) => e === error
          case _                  => false
        }
      }
    }
  }

  test("ConfigError#uncapitalize") {
    forAll(Gen.alphaStr) { (s: String) =>
      val expected =
        if (s.headOption.exists(_.isUpper))
          s"${s.charAt(0).toLower}" ++ s.tail
        else
          s

      assert(ConfigError.uncapitalize(s) === expected)
    }
  }

  test("ConfigError.show") {
    forAll { (error: ConfigError) =>
      assert(error.show === error.toString)
    }
  }
}
