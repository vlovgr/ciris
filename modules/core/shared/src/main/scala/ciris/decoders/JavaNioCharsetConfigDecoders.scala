package ciris.decoders

import java.nio.charset._

import ciris.ConfigDecoder

trait JavaNioCharsetConfigDecoders {
  implicit val charsetConfigDecoder: ConfigDecoder[String, Charset] =
    ConfigDecoder.catchNonFatal[String]("Charset")(Charset.forName)
}
