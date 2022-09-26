/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.kernel.laws.discipline.EqTests
import cats.laws.discipline.TraverseTests
import cats.syntax.all._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll

final class ConfigEntrySpec extends DisciplineSuite with Generators {
  property("ConfigEntry.default.hashCode") {
    val gen =
      for {
        e <- arbitrary[ConfigError]
        e2 <- arbitrary[ConfigError]
        if e =!= e2
        default <- arbitrary[String]
        default2 <- arbitrary[String]
        if default =!= default2
      } yield (e, e2, default, default2)

    forAll(gen) { case (e, e2, default, default2) =>
      val entry1 = ConfigEntry.Default(e, default)
      val entry2 = ConfigEntry.Default(e, default2)
      val entry3 = ConfigEntry.Default(e2, default)
      val entry4 = ConfigEntry.Default(e, default)

      (entry1.hashCode =!= entry2.hashCode) &&
      (entry2.hashCode =!= entry3.hashCode) &&
      (entry1.hashCode =!= entry3.hashCode) &&
      (entry1.hashCode === entry4.hashCode)
    }
  }

  property("ConfigEntry.default.equals.default") {
    val gen =
      for {
        e <- arbitrary[ConfigError]
        e2 <- arbitrary[ConfigError]
        if e =!= e2
        default <- arbitrary[String]
        default2 <- arbitrary[String]
        if default =!= default2
      } yield (e, e2, default, default2)

    forAll(gen) { case (e, e2, default, default2) =>
      val entry1 = ConfigEntry.Default(e, default)
      val entry2 = ConfigEntry.Default(e, default2)
      val entry3 = ConfigEntry.Default(e2, default)
      val entry4 = ConfigEntry.Default(e, default)

      (entry1 != entry2) &&
      (entry2 != entry3) &&
      (entry1 != entry3) &&
      (entry1 == entry4)
    }
  }

  property("ConfigEntry.default.equals.non default") {
    forAll { (e: ConfigError, default: String) =>
      val entry = ConfigEntry.Default(e, default)
      (entry: Any) != e && (entry: Any) != default
    }
  }

  checkAll("ConfigEntry", EqTests[ConfigEntry[String]].eqv)

  property("ConfigEntry.mapError.default") {
    forAll { (error: ConfigError, value: String) =>
      val entry = ConfigEntry.Default(error, value)
      entry.mapError(_.redacted).error === error.redacted
    }
  }

  property("ConfigEntry.mapError.failed") {
    forAll { (error: ConfigError) =>
      val entry = ConfigEntry.Failed(error)
      entry.mapError(_.redacted).error === error.redacted
    }
  }

  property("ConfigEntry.mapError.loaded") {
    forAll { (error: ConfigError, key: Option[ConfigKey], value: String) =>
      val entry = ConfigEntry.Loaded(error, key, value)
      entry.mapError(_.redacted).error === error.redacted
    }
  }

  property("ConfigEntry.show") {
    forAll { (entry: ConfigEntry[String]) =>
      entry.show === entry.toString
    }
  }

  checkAll(
    "ConfigEntry",
    TraverseTests[ConfigEntry].traverse[Int, Int, Int, Int, Option, Option]
  )
}
