/*
 * Copyright 2017-2026 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.IO
import cats.syntax.all._
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.effect.PropF
import org.scalacheck.Gen

final class CirisSpec extends CatsEffectSuite with ScalaCheckEffectSuite with Generators {
  test("default") {
    PropF.forAllF { (value: String) =>
      default(value)
        .to[IO]
        .use {
          case ConfigEntry.Default(ConfigError.Empty, default) => IO.pure(default === value)
          case _                                               => IO.pure(false)
        }
        .assert
    }
  }

  test("prop") {
    val propGen: Gen[String] =
      Gen.oneOf(
        Gen.oneOf(sys.props.keys.toList),
        arbitrary[String]
      )

    PropF.forAllF(propGen) { name =>
      val description = ConfigKey.prop(name).description
      prop(name)
        .to[IO]
        .use {
          case ConfigEntry.Loaded(ConfigError.Loaded, Some(ConfigKey(`description`)), value) =>
            IO(sys.props.get(name).contains(value))

          case ConfigEntry.Failed(ConfigError.Missing(ConfigKey(`description`))) =>
            IO(name.isEmpty || !sys.props.contains(name))

          case _ =>
            IO.pure(false)
        }
        .assert
    }
  }
}
