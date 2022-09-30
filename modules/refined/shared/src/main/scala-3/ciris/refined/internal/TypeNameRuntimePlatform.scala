/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris.refined.internal

import scala.quoted._

private[internal] trait TypeNameRuntimePlatform {
  inline given [A]: TypeName[A] =
    ${TypeNameRuntimePlatform.typeName[A]}
}

private[internal] object TypeNameRuntimePlatform {
  final def typeName[A](using t: Type[A], ctx: Quotes): Expr[TypeName[A]] =
    '{TypeName[A](${Expr(Type.show[A])})}
}
