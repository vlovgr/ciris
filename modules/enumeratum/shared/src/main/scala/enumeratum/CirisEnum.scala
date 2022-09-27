/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import ciris.ConfigDecoder
import enumeratum.internal.TypeName

trait CirisEnum[A <: EnumEntry] { this: Enum[A] =>
  implicit def cirisConfigDecoder(implicit typeName: TypeName[A]): ConfigDecoder[String, A] =
    Ciris.enumConfigDecoder(this)
}
