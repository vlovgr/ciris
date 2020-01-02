/*
 * Copyright 2017-2020 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum

import ciris.ConfigDecoder
import scala.reflect.runtime.universe.WeakTypeTag

trait CirisEnum[A <: EnumEntry] { this: Enum[A] =>
  implicit def cirisConfigDecoder(implicit tag: WeakTypeTag[A]): ConfigDecoder[String, A] =
    Ciris.enumConfigDecoder(this)
}
