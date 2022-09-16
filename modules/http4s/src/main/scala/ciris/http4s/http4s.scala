/*
 * Copyright 2017-2022 Viktor Lövgren
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import org.http4s.Uri
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port

package object http4s {
  implicit final val uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapOption("Uri")(Uri.fromString(_).toOption)

  implicit final val hostConfigDecoder: ConfigDecoder[String, Host] =
    ConfigDecoder[String].mapOption("Host")(Host.fromString)

  implicit final val portConfigDecoder: ConfigDecoder[String, Port] =
    ConfigDecoder[String].mapOption("Host")(Port.fromString)
}
