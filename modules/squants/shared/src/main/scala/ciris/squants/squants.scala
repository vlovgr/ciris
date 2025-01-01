/*
 * Copyright 2017-2025 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import _root_.squants.{Dimension, Quantity}

package object squants {
  implicit final def stringQuantityConfigDecoder[A <: Quantity[A]](
    implicit dimension: Dimension[A]
  ): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapOption(dimension.name) { s => dimension.parseString(s).toOption }
}
