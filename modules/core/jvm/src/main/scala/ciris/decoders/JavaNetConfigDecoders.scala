package ciris.decoders

import java.net._

import ciris.ConfigDecoder
import ciris.ConfigDecoder.catchNonFatal

trait JavaNetConfigDecoders {
  implicit val inetAddressConfigDecoder: ConfigDecoder[InetAddress] =
    catchNonFatal("InetAddress")(InetAddress.getByName)

  implicit val uriConfigDecoder: ConfigDecoder[URI] =
    catchNonFatal("URI")(new URI(_))

  implicit val urlConfigDecoder: ConfigDecoder[URL] =
    catchNonFatal("URL")(new URL(_))
}
