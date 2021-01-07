/*
 * Copyright 2017-2021 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import cats.Show
import ciris.ConfigDecoder
import scala.reflect.runtime.universe.WeakTypeTag

final object Ciris {
  final def enumConfigDecoder[ValueType, EntryType <: ValueEnumEntry[ValueType]](
    enum: ValueEnum[ValueType, EntryType]
  )(
    implicit decoder: ConfigDecoder[String, ValueType],
    tag: WeakTypeTag[EntryType],
    show: Show[ValueType]
  ): ConfigDecoder[String, EntryType] = {
    val typeName = tag.tpe.typeSymbol.name.decodedName.toString
    decoder.mapOption(typeName)(enum.withValueOpt)
  }
}
