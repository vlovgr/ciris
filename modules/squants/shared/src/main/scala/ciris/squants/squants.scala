/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import _root_.squants.{Dimension, Quantity}

package object squants {
  
  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit final def stringQuantityConfigDecoder[A <: Quantity[A]](
    implicit dimension: Dimension[A]
  ): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapOption(dimension.name) { s => dimension.parseString(s).toOption }

  implicit final def stringQuantityConfigCodec[A <: Quantity[A]](
    implicit dimension: Dimension[A]
  ): ConfigCodec[String, A] =
    ConfigCodec[String].imapOption(dimension.name)(s => dimension.parseString(s).toOption)(_.toString())
}
