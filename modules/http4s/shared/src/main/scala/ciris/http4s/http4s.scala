/*
 * Copyright 2017-2024 Viktor Rudebeck
 *
 * SPDX-License-Identifier: MIT
 */

package ciris

import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import org.http4s.Uri

package object http4s {

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit final val hostConfigDecoder: ConfigDecoder[String, Host] =
    ConfigDecoder[String].mapOption("Host")(Host.fromString)

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit final val portConfigDecoder: ConfigDecoder[String, Port] =
    ConfigDecoder[String].mapOption("Port")(Port.fromString)

  @deprecated("Use ConfigCodec instead", "3.7.0")
  implicit final val uriConfigDecoder: ConfigDecoder[String, Uri] =
    ConfigDecoder[String].mapOption("Uri")(Uri.fromString(_).toOption)

  implicit final val hostConfigCodec: ConfigCodec[String, Host] =
    ConfigCodec[String].imapOption("Host")(Host.fromString)(_.toString)

  implicit final val portConfigCodec: ConfigCodec[String, Port] =
    ConfigCodec[String].imapOption("Port")(Port.fromString)(_.toString())

  implicit final val uriConfigCodec: ConfigCodec[String, Uri] =
    ConfigCodec[String].imapOption("Uri")(Uri.fromString(_).toOption)(_.renderString)
}
