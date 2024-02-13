/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.refined

import cats.effect.IO
import cats.syntax.all._
import ciris._
import eu.timepit.refined.types.numeric.PosInt
import munit.CatsEffectSuite

final class RefinedSpec extends CatsEffectSuite {
  test("refTypeConfigDecoder.success") {
    default("1")
      .as[PosInt]
      .attempt[IO]
      .map(_.map(_.value))
      .assertEquals(Right(1))
  }

  test("refTypeConfigDecoder.error") {
    default("0")
      .as[PosInt]
      .attempt[IO]
      .map(_.leftMap(_.messages.exists(_.contains("Refined"))))
      .assertEquals(Left(true))
  }
}
