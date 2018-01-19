package ciris.decoders

import java.nio.charset._

import ciris.ConfigDecoder

trait JavaNioCharsetConfigDecoders {
  implicit val charsetConfigDecoder: ConfigDecoder[Charset] =
    ConfigDecoder.catchNonFatal("Charset")(Charset.forName)
}
