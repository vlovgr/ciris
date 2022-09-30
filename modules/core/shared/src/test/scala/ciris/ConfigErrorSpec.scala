/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.data.Chain
import cats.kernel.laws.discipline.EqTests
import cats.syntax.all._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

final class ConfigErrorSpec extends DisciplineSuite with Generators {
  property("ConfigError.and.messages") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.And(errors).messages
      messages.size == errors.foldMap(_.messages.size) &&
      errors.forall(_.messages.forall { m => messages.exists(_.contains(m)) })
    }
  }

  property("ConfigError.and.redacted") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.And(errors).redacted.messages
      messages.size == errors.foldMap(_.messages.size) &&
      errors.map(_.redacted).forall(_.messages.forall { m => messages.exists(_.contains(m)) })
    }
  }

  property("ConfigError.and.toString") {
    forAll { (errors: Chain[ConfigError]) =>
      val error = ConfigError.And(errors)
      error.toString === s"And(${errors.toList.mkString(", ")})"
    }
  }

  property("ConfigError.apply.messages") {
    forAll { (message: String) =>
      val messages = ConfigError(message).messages
      messages.size == 1 && messages.toList.head === message
    }
  }

  property("ConfigError.apply.redacted") {
    forAll { (message: String) =>
      val messages = ConfigError(message).redacted.messages
      messages.size == 1 && messages.toList.head === message
    }
  }

  property("ConfigError.apply.hashCode") {
    val gen =
      for {
        message <- arbitrary[String]
        message2 <- arbitrary[String]
        if message =!= message2
      } yield (message, message2)

    forAll(gen) { case (message, message2) =>
      val error = ConfigError(message)
      val error2 = ConfigError(message2)
      val error3 = ConfigError(message)

      (error.hashCode =!= error2.hashCode) &&
      (error2.hashCode =!= error3.hashCode) &&
      (error.hashCode === error3.hashCode)
    }
  }

  property("ConfigError.apply.equals") {
    val gen =
      for {
        message <- arbitrary[String]
        message2 <- arbitrary[String]
        if message =!= message2
      } yield (message, message2)

    forAll(gen) { case (message, message2) =>
      val error = ConfigError(message)
      val error2 = ConfigError(message2)
      val error3 = ConfigError(message)

      (error != error2) &&
      (error2 != error3) &&
      (error == error3)
    }
  }

  property("ConfigError.apply.toString") {
    forAll { (message: String) =>
      val error = ConfigError(message)
      error.toString === s"ConfigError($message)"
    }
  }

  property("ConfigError.decode.key") {
    forAll { (typeName: String, key: ConfigKey, value: String) =>
      val error = ConfigError.decode(typeName, Some(key), value)
      error.messages === Chain
        .one(s"${key.description.capitalize} with value $value cannot be converted to $typeName")
    }
  }

  property("ConfigError.decode.key redacted") {
    forAll { (typeName: String, key: ConfigKey, value: String) =>
      val error = ConfigError.decode(typeName, Some(key), value)
      error.redacted.messages === Chain
        .one(s"${key.description.capitalize} cannot be converted to $typeName")
    }
  }

  property("ConfigError.decode.no key") {
    forAll { (typeName: String, value: String) =>
      val error = ConfigError.decode(typeName, None, value)
      error.messages === Chain.one(s"Unable to convert value $value to $typeName")
    }
  }

  property("ConfigError.decode.no key redacted") {
    forAll { (typeName: String, value: String) =>
      val error = ConfigError.decode(typeName, None, value)
      error.redacted.messages === Chain.one(s"Unable to convert value to $typeName")
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

  property("ConfigError.missing.messages") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      assert(error.messages.size == 1)

      val message = error.messages.toList.head
      message === s"Missing ${ConfigError.uncapitalize(key.description)}"
    }
  }

  property("ConfigError.missing.redacted") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      error.redacted === error
    }
  }

  property("ConfigError.missing.toString") {
    forAll { (key: ConfigKey) =>
      val error = ConfigError.Missing(key)
      error.toString === s"Missing($key)"
    }
  }

  property("ConfigError.normalize") {
    forAll { (errors: Chain[ConfigError]) =>
      val actual =
        ConfigError.normalize(errors, ConfigError.And(_))

      val expected =
        if (errors.isEmpty)
          ConfigError.And(errors)
        else if (errors.forall(_ === ConfigError.Loaded))
          ConfigError.Loaded
        else if (errors.contains(ConfigError.Loaded))
          ConfigError.And(errors.filterNot(_ === ConfigError.Loaded).prepend(ConfigError.Loaded))
        else
          ConfigError.And(errors)

      actual === expected
    }
  }

  property("ConfigError.or.messages") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.Or(errors).messages
      messages.size == 1
    }
  }

  property("ConfigError.or.redacted") {
    forAll { (errors: Chain[ConfigError]) =>
      val messages = ConfigError.Or(errors).redacted.messages
      messages.size == 1 && {
        val message = messages.toList.head.toLowerCase
        errors
          .map(_.redacted)
          .forall(_.messages.forall { m => message.contains(m.toLowerCase) })
      }
    }
  }

  property("ConfigError.or.toString") {
    forAll { (errors: Chain[ConfigError]) =>
      val error = ConfigError.Or(errors)
      error.toString === s"Or(${errors.toList.mkString(", ")})"
    }
  }

  property("ConfigError.sensitive.messages") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      error.messages === Chain.one(message)
    }
  }

  property("ConfigError.sensitive.redacted") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      error.redacted.messages === Chain.one(redactedMessage)
    }
  }

  property("ConfigError.sensitive.hashCode") {
    val gen =
      for {
        message <- arbitrary[String]
        redactedMessage <- arbitrary[String]
        if message =!= redactedMessage
      } yield (message, redactedMessage)

    forAll(gen) { case (message, redactedMessage) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      val error2 = ConfigError.sensitive(message, message)
      val error3 = ConfigError.sensitive(redactedMessage, redactedMessage)

      (error.hashCode =!= error2.hashCode) &&
      (error2.hashCode =!= error3.hashCode) &&
      (error.hashCode =!= error3.hashCode)
    }
  }

  property("ConfigError.sensitive.equals.sensitive") {
    val gen =
      for {
        message <- arbitrary[String]
        redactedMessage <- arbitrary[String]
        if message =!= redactedMessage
      } yield (message, redactedMessage)

    forAll(gen) { case (message, redactedMessage) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      val error2 = ConfigError.sensitive(message, message)
      val error3 = ConfigError.sensitive(redactedMessage, redactedMessage)
      val error4 = ConfigError.sensitive(message, redactedMessage)

      (error != error2) &&
      (error2 != error3) &&
      (error != error3) &&
      (error == error4)
    }
  }

  property("ConfigError.sensitive.equals.non sensitive") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      (error: Any) != message && (error: Any) != redactedMessage
    }
  }

  property("ConfigError.sensitive.toString") {
    forAll { (message: String, redactedMessage: String) =>
      val error = ConfigError.sensitive(message, redactedMessage)
      error.toString === s"Sensitive($message, $redactedMessage)"
    }
  }

  property("ConfigError#throwable") {
    forAll { (error: ConfigError) =>
      error.throwable match {
        case ConfigException(e) => e === error
        case _                  => false
      }
    }
  }

  property("ConfigError#uncapitalize") {
    forAll(Gen.alphaStr) { (s: String) =>
      val expected =
        if (s.headOption.exists(_.isUpper))
          s"${s.charAt(0).toLower}" ++ s.tail
        else
          s

      ConfigError.uncapitalize(s) === expected
    }
  }

  property("ConfigError.show") {
    forAll { (error: ConfigError) =>
      error.show === error.toString
    }
  }
}
