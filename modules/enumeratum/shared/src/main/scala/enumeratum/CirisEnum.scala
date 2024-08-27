/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import ciris.ConfigDecoder
import enumeratum.internal.TypeName
import ciris.ConfigCodec

trait CirisEnum[A <: EnumEntry] { this: Enum[A] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit def cirisConfigDecoder(implicit typeName: TypeName[A]): ConfigDecoder[String, A] =
    Ciris.enumConfigDecoder(this)

  implicit def cirisConfigCodec(implicit typeName: TypeName[A]): ConfigCodec[String, A] =
    Ciris.enumConfigCodec(this)
}
