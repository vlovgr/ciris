/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import ciris.ConfigDecoder
import enumeratum.internal.TypeName

object Ciris {
  final def enumConfigDecoder[A <: EnumEntry](
    `enum`: Enum[A]
  )(implicit typeName: TypeName[A]): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapOption(typeName.value)(`enum`.withNameOption)
}
