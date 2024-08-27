/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import ciris.ConfigDecoder
import enumeratum.internal.TypeName
import ciris.ConfigCodec

object Ciris {

  @deprecated("Use ConfigCodec instead", "3.7.0")
  final def enumConfigDecoder[A <: EnumEntry](
    `enum`: Enum[A]
  )(implicit typeName: TypeName[A]): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapOption(typeName.value)(`enum`.withNameOption)

  final def enumConfigCodec[A <: EnumEntry](`enum`: Enum[A])(implicit typeName: TypeName[A]): ConfigCodec[String, A] =
    ConfigCodec[String].imapOption(typeName.value)(`enum`.withNameOption)(_.entryName)
}
