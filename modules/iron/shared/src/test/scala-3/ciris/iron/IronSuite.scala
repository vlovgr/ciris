/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.iron

import ciris.*
import ciris.iron.given
import io.github.iltotore.iron.*
import munit.FunSuite

final class IronSuite extends FunSuite {

  test("summon String => Int :| Pure") {
    summon[ConfigDecoder[String, Int :| Pure]]

  }

  test("summon from Int => Int :| Pure") {
    summon[ConfigDecoder[Int, Int :| Pure]]
  }
}
