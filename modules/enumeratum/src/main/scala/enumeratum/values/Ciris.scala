/*
 * Copyright 2017-2021 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import cats.Show
import ciris.ConfigDecoder
import org.tpolecat.typename.TypeName

object Ciris {
  final def enumConfigDecoder[ValueType, EntryType <: ValueEnumEntry[ValueType]](
    `enum`: ValueEnum[ValueType, EntryType]
  )(
    implicit decoder: ConfigDecoder[String, ValueType],
    typeName: TypeName[EntryType],
    show: Show[ValueType]
  ): ConfigDecoder[String, EntryType] =
    decoder.mapOption(typeName.value)(enum.withValueOpt)
}
