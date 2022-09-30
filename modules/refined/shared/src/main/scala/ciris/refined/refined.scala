/*
 * Copyright 2017-2022 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import cats.Show
import ciris.refined.internal.TypeName
import eu.timepit.refined.api.{RefType, Validate}

package object refined {
  implicit final def refTypeConfigDecoder[F[_, _], A, B, P](
    implicit decoder: ConfigDecoder[A, B],
    refType: RefType[F],
    show: Show[B],
    validate: Validate[B, P],
    typeName: TypeName[F[B, P]]
  ): ConfigDecoder[A, F[B, P]] = {
    val refine = refType.refine[P]
    decoder.mapOption(typeName.value)(refine(_).toOption)
  }
}
