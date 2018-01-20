package ciris.decoders

import java.net._

import ciris.ConfigDecoder

trait JavaNetConfigDecoders {
  implicit val inetAddressConfigDecoder: ConfigDecoder[String, InetAddress] =
    ConfigDecoder.catchNonFatal[String]("InetAddress")(InetAddress.getByName)

  implicit val uriConfigDecoder: ConfigDecoder[String, URI] =
    ConfigDecoder.catchNonFatal[String]("URI")(new URI(_))

  implicit val urlConfigDecoder: ConfigDecoder[String, URL] =
    ConfigDecoder.catchNonFatal[String]("URL")(new URL(_))
}
