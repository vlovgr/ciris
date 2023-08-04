/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.iron

import cats.effect.IO
import cats.syntax.all.*
import ciris.*
import ciris.iron.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Positive
import munit.{CatsEffectSuite, FunSuite}

final class IronSuite extends CatsEffectSuite {

  test("ironConfigDecoder.success") {

    default("1")
      .as[Int :| Positive]
      .attempt[IO]
      .assertEquals(Right(1))
  }

  test("ironConfigDecoder.error") {

    default("-1")
      .as[Int :| Positive]
      .attempt[IO]
      .map(_.isLeft)
      .assertEquals(true)
  }

}
