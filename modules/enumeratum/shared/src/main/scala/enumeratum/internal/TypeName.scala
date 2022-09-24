/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package enumeratum.internal

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

final case class TypeName[A](value: String)

object TypeName {
  implicit final def typeName[A]: TypeName[A] =
    macro typeNameImpl[A]

  final def typeNameImpl[A](c: Context): c.Expr[TypeName[A]] = {
    import c.universe._
    val TypeApply(_, List(typeTree)) = c.macroApplication: @unchecked
    c.Expr(q"_root_.enumeratum.internal.TypeName(${typeTree.toString})")
  }
}
