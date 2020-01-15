/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import cats.implicits._
import ciris.ConfigDecoder
import scala.reflect.runtime.universe.WeakTypeTag

final object Ciris {
  final def enumConfigDecoder[A <: EnumEntry](
    enum: Enum[A]
  )(implicit tag: WeakTypeTag[A]): ConfigDecoder[String, A] = {
    val typeName = tag.tpe.typeSymbol.name.decodedName.toString
    ConfigDecoder[String].mapOption(typeName)(enum.withNameOption)
  }
}
