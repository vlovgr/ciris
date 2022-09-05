/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.kernel.laws.discipline.EqTests
import cats.tests.CatsSuite

final class ConfigKeySpec extends CatsSuite with Generators {
  test("ConfigKey.description") {
    forAll { (description: String) =>
      assert(ConfigKey(description).description === description)
    }
  }

  test("ConfigKey.env") {
    forAll { (name: String) =>
      val description = ConfigKey.env(name).description
      assert(description === s"environment variable $name")
    }
  }

  checkAll("ConfigKey", EqTests[ConfigKey].eqv)

  test("ConfigKey.equals.key") {
    forAll { (first: ConfigKey, second: ConfigKey) =>
      val expected = first.description == second.description
      val actual = first == second
      assert(expected === actual)
    }
  }

  test("ConfigKey.equals.non key") {
    forAll { (key: ConfigKey) =>
      assert((key: Any) != key.description)
    }
  }

  test("ConfigKey.hashCode") {
    forAll { (key: ConfigKey) =>
      assert(key.hashCode === key.description.hashCode)
    }
  }

  test("ConfigKey.prop") {
    forAll { (name: String) =>
      val description = ConfigKey.prop(name).description
      assert(description === s"system property $name")
    }
  }

  test("ConfigKey.show") {
    forAll { (key: ConfigKey) =>
      assert(key.show === key.toString)
    }
  }

  test("ConfigKey.toString") {
    forAll { (key: ConfigKey) =>
      assert(key.toString === s"ConfigKey(${key.description})")
    }
  }

  test("ConfigKey.unapply") {
    forAll { (key: ConfigKey) =>
      key match {
        case ConfigKey(description) =>
          assert(description === key.description)
      }
    }
  }
}
