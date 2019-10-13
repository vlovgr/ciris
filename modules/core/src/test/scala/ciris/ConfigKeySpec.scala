package ciris

import cats.kernel.laws.discipline.EqTests
import java.nio.charset.Charset
import java.nio.file.Path

final class ConfigKeySpec extends BaseSpec {
  test("ConfigKey.description") {
    forAll { description: String =>
      assert(ConfigKey(description).description === description)
    }
  }

  test("ConfigKey.env") {
    forAll { name: String =>
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
    forAll { key: ConfigKey =>
      assert((key: Any) != key.description)
    }
  }

  test("ConfigKey.file") {
    forAll { (path: Path, charset: Charset) =>
      val description = ConfigKey.file(path, charset).description
      assert(description === s"file at $path with charset $charset")
    }
  }

  test("ConfigKey.hashCode") {
    forAll { key: ConfigKey =>
      assert(key.hashCode === key.description.hashCode)
    }
  }

  test("ConfigKey.prop") {
    forAll { name: String =>
      val description = ConfigKey.prop(name).description
      assert(description === s"system property $name")
    }
  }

  test("ConfigKey.show") {
    forAll { key: ConfigKey =>
      assert(key.show === key.toString)
    }
  }

  test("ConfigKey.toString") {
    forAll { key: ConfigKey =>
      assert(key.toString === s"ConfigKey(${key.description})")
    }
  }

  test("ConfigKey.unapply") {
    forAll { key: ConfigKey =>
      key match {
        case ConfigKey(description) =>
          assert(description === key.description)
      }
    }
  }
}
