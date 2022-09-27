/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.kernel.laws.discipline.EqTests
import cats.syntax.all._
import org.scalacheck.Prop.forAll

final class ConfigKeySpec extends DisciplineSuite with Generators {
  property("ConfigKey.description") {
    forAll { (description: String) =>
      ConfigKey(description).description === description
    }
  }

  property("ConfigKey.env") {
    forAll { (name: String) =>
      val description = ConfigKey.env(name).description
      description === s"environment variable $name"
    }
  }

  checkAll("ConfigKey", EqTests[ConfigKey].eqv)

  property("ConfigKey.equals.key") {
    forAll { (first: ConfigKey, second: ConfigKey) =>
      val expected = first.description == second.description
      val actual = first == second
      expected === actual
    }
  }

  property("ConfigKey.equals.non key") {
    forAll { (key: ConfigKey) =>
      (key: Any) != key.description
    }
  }

  property("ConfigKey.hashCode") {
    forAll { (key: ConfigKey) =>
      key.hashCode === key.description.hashCode
    }
  }

  property("ConfigKey.prop") {
    forAll { (name: String) =>
      val description = ConfigKey.prop(name).description
      description === s"system property $name"
    }
  }

  property("ConfigKey.show") {
    forAll { (key: ConfigKey) =>
      key.show === key.toString
    }
  }

  property("ConfigKey.toString") {
    forAll { (key: ConfigKey) =>
      key.toString === s"ConfigKey(${key.description})"
    }
  }

  property("ConfigKey.unapply") {
    forAll { (key: ConfigKey) =>
      key match {
        case ConfigKey(description) =>
          description === key.description
      }
    }
  }
}
