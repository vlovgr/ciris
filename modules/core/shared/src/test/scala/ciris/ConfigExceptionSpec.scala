/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.kernel.laws.discipline.EqTests
import cats.syntax.all._
import org.scalacheck.Prop.forAll

final class ConfigExceptionSpec extends DisciplineSuite with Generators {
  checkAll("ConfigException", EqTests[ConfigException].eqv)

  property("ConfigException.error") {
    forAll { (error: ConfigError) =>
      ConfigException(error).error === error
    }
  }

  property("ConfigException.message.contains") {
    forAll { (exception: ConfigException) =>
      val exceptionMessage =
        exception.getMessage

      val messages =
        exception.error.messages

      val withEntryTrailing =
        messages
          .filter(_.endsWith(ConfigException.entryTrailing))
          .forall { (message) =>
            exceptionMessage.contains {
              s"${ConfigException.entryLeading}$message"
            }
          }

      val withoutEntryTrailing =
        messages
          .filterNot(_.endsWith(ConfigException.entryTrailing))
          .forall { (message) =>
            exceptionMessage.contains {
              s"${ConfigException.entryLeading}$message${ConfigException.entryTrailing}"
            }
          }

      withEntryTrailing && withoutEntryTrailing
    }
  }

  property("ConfigException.message.leading") {
    forAll { (exception: ConfigException) =>
      exception.getMessage.startsWith(ConfigException.messageLeading)
    }
  }

  property("ConfigException.message.trailing") {
    forAll { (exception: ConfigException) =>
      exception.getMessage.endsWith(ConfigException.messageTrailing)
    }
  }

  property("ConfigException.messageLength") {
    forAll { (exception: ConfigException) =>
      val expected = ConfigException.messageLength(exception.error.messages)
      val actual = exception.getMessage.length
      actual === expected
    }
  }

  property("ConfigException.toString") {
    forAll { (exception: ConfigException) =>
      exception.toString === s"ciris.ConfigException: ${exception.getMessage}"
    }
  }

  property("ConfigException.unapply") {
    forAll { (exception: ConfigException) =>
      exception match {
        case ConfigException(error) =>
          error === exception.error
      }
    }
  }
}
