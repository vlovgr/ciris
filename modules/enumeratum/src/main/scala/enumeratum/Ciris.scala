/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import cats.implicits._
import ciris.ConfigDecoder
import org.tpolecat.typename.TypeName

object Ciris {
  final def enumConfigDecoder[A <: EnumEntry](
    enum: Enum[A]
  )(implicit typeName: TypeName[A]): ConfigDecoder[String, A] =
    ConfigDecoder[String].mapOption(typeName.value)(enum.withNameOption)
}
