/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.effect.IO
import cats.syntax.all._
import munit.{CatsEffectSuite, ScalaCheckEffectSuite}
import org.scalacheck.effect.PropF

final class UseOnceSecretSpec extends CatsEffectSuite with ScalaCheckEffectSuite {
  test("UseOnceSecret.nullifies") {
    PropF.forAllF { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .flatMap(_.useOnce(_ => IO.unit))
        .flatMap(_ => IO(value.forall(_ == ' ')))
        .assert
    }
  }

  test("UseOnceSecret.useOnce") {
    PropF.forAllF { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .flatMap(_.useOnce(s => IO(s eq value)))
        .assert
    }
  }

  test("UseOnceSecret.useTwice") {
    PropF.forAllF { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .map(_.useOnce(_ => IO.unit))
        .flatMap(use => (use, use).parTupled)
        .attemptNarrow[IllegalStateException]
        .map(_.isLeft)
        .assert
    }
  }
}
