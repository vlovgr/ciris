package ciris.decoders

import java.nio.file._

import ciris.ConfigDecoder

private[ciris] trait JavaNioFileConfigDecoders {
  implicit val pathConfigDecoder: ConfigDecoder[String, Path] =
    ConfigDecoder.catchNonFatal("Path")(Paths.get(_))
}
