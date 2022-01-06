/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import org.http4s.Uri
import org.tpolecat.typename.typeName

package object http4s {
  implicit final val uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapOption(typeName[Uri])(Uri.fromString(_).toOption)
}
