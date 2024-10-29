/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import ciris.ConfigCodec
import ciris.ConfigDecoder
import enumeratum.internal.TypeName

sealed trait CirisValueEnum[ValueType, EntryType <: ValueEnumEntry[ValueType]] {
  this: ValueEnum[ValueType, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType]

  implicit def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType]
}

trait ByteCirisEnum[EntryType <: ByteEnumEntry] extends CirisValueEnum[Byte, EntryType] {
  this: ValueEnum[Byte, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}

trait CharCirisEnum[EntryType <: CharEnumEntry] extends CirisValueEnum[Char, EntryType] {
  this: ValueEnum[Char, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}

trait IntCirisEnum[EntryType <: IntEnumEntry] extends CirisValueEnum[Int, EntryType] {
  this: ValueEnum[Int, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}

trait LongCirisEnum[EntryType <: LongEnumEntry] extends CirisValueEnum[Long, EntryType] {
  this: ValueEnum[Long, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}

trait ShortCirisEnum[EntryType <: ShortEnumEntry] extends CirisValueEnum[Short, EntryType] {
  this: ValueEnum[Short, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}

trait StringCirisEnum[EntryType <: StringEnumEntry] extends CirisValueEnum[String, EntryType] {
  this: ValueEnum[String, EntryType] =>

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)

  implicit override def cirisConfigCodec(
    implicit typeName: TypeName[EntryType]
  ): ConfigCodec[String, EntryType] =
    Ciris.enumConfigCodec(this)
}
