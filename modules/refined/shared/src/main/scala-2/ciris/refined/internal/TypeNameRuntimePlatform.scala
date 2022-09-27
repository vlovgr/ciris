/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.refined.internal

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

private[internal] trait TypeNameRuntimePlatform {
  implicit final def typeName[A]: TypeName[A] =
    macro TypeNameRuntimePlatform.typeName[A]
}

private[internal] object TypeNameRuntimePlatform {
  final def typeName[A](c: Context): c.Expr[TypeName[A]] = {
    import c.universe._
    val TypeApply(_, List(typeTree)) = c.macroApplication: @unchecked
    c.Expr(q"_root_.ciris.refined.internal.TypeName(${typeTree.toString})")
  }
}
