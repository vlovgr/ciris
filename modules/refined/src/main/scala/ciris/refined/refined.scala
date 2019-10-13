/*
 * Copyright 2017-2019 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.Show
import eu.timepit.refined.api.{RefType, Validate}
import scala.reflect.runtime.universe.WeakTypeTag

package object refined {
  implicit final def refTypeConfigDecoder[F[_, _], A, B, P](
    implicit decoder: ConfigDecoder[A, B],
    refType: RefType[F],
    show: Show[B],
    validate: Validate[B, P],
    typeTag: WeakTypeTag[F[B, P]]
  ): ConfigDecoder[A, F[B, P]] = {
    val refine = refType.refine[P]
    val typeName = typeTag.tpe.toString
    decoder.mapOption(typeName)(refine(_).toOption)
  }
}
