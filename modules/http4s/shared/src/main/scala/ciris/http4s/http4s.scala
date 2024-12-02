/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.Uri
import org.http4s.headers.Origin

package object http4s {
  implicit final val hostConfigDecoder: ConfigDecoder[String, Host] =
    ConfigDecoder[String].mapOption("Host")(Host.fromString)

  implicit final val portConfigDecoder: ConfigDecoder[String, Port] =
    ConfigDecoder[String].mapOption("Port")(Port.fromString)

  implicit final val uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapOption("Uri")(Uri.fromString(_).toOption)

  implicit final val originConfigDecoder: ConfigDecoder[String, Origin] =
    ConfigDecoder[String].mapOption("Origin")(Origin.parse(_).toOption)
}
