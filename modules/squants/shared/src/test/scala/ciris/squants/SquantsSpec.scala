/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.squants

import _root_.squants.time.Time
import cats.effect.IO
import ciris._
import munit.CatsEffectSuite

final class SquantsSpec extends CatsEffectSuite {
  test("stringQuantityConfigDecoder.success") {
    default("1s")
      .as[Time]
      .attempt[IO]
      .map(_.toOption)
      .assertEquals(Time.parseString("1s").toOption)
  }

  test("stringQuantityConfigDecoder.error") {
    default("1")
      .as[Time]
      .attempt[IO]
      .assertEquals {
        Left {
          ConfigError.sensitive(
            "Unable to convert value 1 to Time",
            "Unable to convert value to Time"
          )
        }
      }
  }
}
