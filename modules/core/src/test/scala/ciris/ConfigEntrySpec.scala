package ciris

import cats.implicits._
import cats.kernel.laws.discipline.EqTests
import cats.laws.discipline.TraverseTests

final class ConfigEntrySpec extends BaseSpec {
  test("ConfigEntry.default.hashCode") {
    forAll { (e: ConfigError, e2: ConfigError, default: String, default2: String) =>
      whenever((e !== e2) && (default != default2)) {
        val entry1 = ConfigEntry.Default(e, () => default)
        val entry2 = ConfigEntry.Default(e, () => default2)
        val entry3 = ConfigEntry.Default(e2, () => default)
        val entry4 = ConfigEntry.Default(e, () => default)

        assert {
          (entry1.hashCode !== entry2.hashCode) &&
          (entry2.hashCode !== entry3.hashCode) &&
          (entry1.hashCode !== entry3.hashCode) &&
          (entry1.hashCode === entry4.hashCode)
        }
      }
    }
  }

  test("ConfigEntry.default.equals.default") {
    forAll { (e: ConfigError, e2: ConfigError, default: String, default2: String) =>
      whenever((e !== e2) && (default != default2)) {
        val entry1 = ConfigEntry.Default(e, () => default)
        val entry2 = ConfigEntry.Default(e, () => default2)
        val entry3 = ConfigEntry.Default(e2, () => default)
        val entry4 = ConfigEntry.Default(e, () => default)

        assert {
          (entry1 != entry2) &&
          (entry2 != entry3) &&
          (entry1 != entry3) &&
          (entry1 == entry4)
        }
      }
    }
  }

  test("ConfigEntry.default.equals.non default") {
    forAll { (e: ConfigError, default: String) =>
      val entry = ConfigEntry.Default(e, () => default)
      assert((entry: Any) != e && (entry: Any) != default)
    }
  }

  checkAll("ConfigEntry", EqTests[ConfigEntry[String]].eqv)

  test("ConfigEntry.mapError.default") {
    forAll { (error: ConfigError, value: String) =>
      val entry = ConfigEntry.Default(error, () => value)
      assert(entry.mapError(_.redacted).error === error.redacted)
    }
  }

  test("ConfigEntry.mapError.failed") {
    forAll { error: ConfigError =>
      val entry = ConfigEntry.Failed(error)
      assert(entry.mapError(_.redacted).error === error.redacted)
    }
  }

  test("ConfigEntry.mapError.loaded") {
    forAll { (error: ConfigError, key: Option[ConfigKey], value: String) =>
      val entry = ConfigEntry.Loaded(error, key, value)
      assert(entry.mapError(_.redacted).error === error.redacted)
    }
  }

  test("ConfigEntry.show") {
    forAll { entry: ConfigEntry[String] => assert(entry.show === entry.toString) }
  }

  checkAll(
    "ConfigEntry",
    TraverseTests[ConfigEntry].traverse[Int, Int, Int, Int, Option, Option]
  )
}
