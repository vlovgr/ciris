/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.values

import ciris.ConfigDecoder
import enumeratum.internal.TypeName

sealed trait CirisValueEnum[ValueType, EntryType <: ValueEnumEntry[ValueType]] {
  this: ValueEnum[ValueType, EntryType] =>

  implicit def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType]
}

trait ByteCirisEnum[EntryType <: ByteEnumEntry] extends CirisValueEnum[Byte, EntryType] {
  this: ValueEnum[Byte, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}

trait CharCirisEnum[EntryType <: CharEnumEntry] extends CirisValueEnum[Char, EntryType] {
  this: ValueEnum[Char, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}

trait IntCirisEnum[EntryType <: IntEnumEntry] extends CirisValueEnum[Int, EntryType] {
  this: ValueEnum[Int, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}

trait LongCirisEnum[EntryType <: LongEnumEntry] extends CirisValueEnum[Long, EntryType] {
  this: ValueEnum[Long, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}

trait ShortCirisEnum[EntryType <: ShortEnumEntry] extends CirisValueEnum[Short, EntryType] {
  this: ValueEnum[Short, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}

trait StringCirisEnum[EntryType <: StringEnumEntry] extends CirisValueEnum[String, EntryType] {
  this: ValueEnum[String, EntryType] =>

  implicit override def cirisConfigDecoder(
    implicit typeName: TypeName[EntryType]
  ): ConfigDecoder[String, EntryType] =
    Ciris.enumConfigDecoder(this)
}
