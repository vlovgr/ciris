/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.squants

import _root_.squants.time.Time
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import ciris._
import org.scalatest.funsuite.AnyFunSuite

final class SquantsSpec extends AnyFunSuite {
  test("stringQuantityConfigDecoder.success") {
    assert {
      val actual = default("1s").as[Time].attempt[IO].unsafeRunSync().toOption
      val expected = Time.parseString("1s").toOption
      actual == expected
    }
  }

  test("stringQuantityConfigDecoder.error") {
    assert {
      val actual = default("1").as[Time].attempt[IO].unsafeRunSync()

      val expected =
        Left {
          ConfigError.sensitive(
            "Unable to convert value 1 to Time",
            "Unable to convert value to Time"
          )
        }

      actual == expected
    }
  }
}
