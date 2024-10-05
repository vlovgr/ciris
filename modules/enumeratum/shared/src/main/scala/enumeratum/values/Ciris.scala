/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import cats.Show
import ciris.ConfigCodec
import ciris.ConfigDecoder
import enumeratum.internal.TypeName

object Ciris {

  @deprecated("Use ConfigCodec and enumConfigCodec instead", "3.7.0")
  final def enumConfigDecoder[ValueType, EntryType <: ValueEnumEntry[ValueType]](
    `enum`: ValueEnum[ValueType, EntryType]
  )(
    implicit decoder: ConfigDecoder[String, ValueType],
    typeName: TypeName[EntryType],
    show: Show[ValueType]
  ): ConfigDecoder[String, EntryType] =
    decoder.mapOption(typeName.value)(`enum`.withValueOpt)

  final def enumConfigCodec[ValueType, EntryType <: ValueEnumEntry[ValueType]](
    `enum`: ValueEnum[ValueType, EntryType]
  )(
    implicit codec: ConfigCodec[String, ValueType],
    typeName: TypeName[EntryType],
    show: Show[ValueType]
  ): ConfigCodec[String, EntryType] =
    codec.imapOption(typeName.value)(`enum`.withValueOpt)(_.value)
}
