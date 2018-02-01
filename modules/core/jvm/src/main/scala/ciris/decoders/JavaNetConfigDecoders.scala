package ciris.decoders

import java.net._

import ciris.ConfigDecoder

private[ciris] trait JavaNetConfigDecoders {
  implicit val inetAddressConfigDecoder: ConfigDecoder[String, InetAddress] =
    ConfigDecoder.catchNonFatal("InetAddress")(InetAddress.getByName)

  implicit val uriConfigDecoder: ConfigDecoder[String, URI] =
    ConfigDecoder.catchNonFatal("URI")(new URI(_))

  implicit val urlConfigDecoder: ConfigDecoder[String, URL] =
    ConfigDecoder.catchNonFatal("URL")(new URL(_))
}
