/*
 * Copyright 2017-2023 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.iron

import cats.Show
import ciris.ConfigDecoder
import io.github.iltotore.iron.*

inline given [T, A, B](
  using inline decoder: ConfigDecoder[T, A],
  inline constraint: _root_.io.github.iltotore.iron.Constraint[A, B],
  inline show: Show[A]
): ConfigDecoder[T, A :| B] =
  decoder.mapOption("")(_.refineOption)
