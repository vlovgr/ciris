package ciris.decoders

import java.nio.charset._

import ciris.ConfigDecoder

private[ciris] trait JavaNioCharsetConfigDecoders {
  implicit val charsetConfigDecoder: ConfigDecoder[String, Charset] =
    ConfigDecoder.catchNonFatal("Charset")(Charset.forName)
}
