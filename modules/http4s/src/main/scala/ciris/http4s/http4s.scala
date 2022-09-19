/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.Uri

package object http4s {
  implicit final val hostConfigDecoder: ConfigDecoder[String, Host] =
    ConfigDecoder[String].mapOption("Host")(Host.fromString)

  implicit final val portConfigDecoder: ConfigDecoder[String, Port] =
    ConfigDecoder[String].mapOption("Port")(Port.fromString)

  implicit final val uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapOption("Uri")(Uri.fromString(_).toOption)
}
