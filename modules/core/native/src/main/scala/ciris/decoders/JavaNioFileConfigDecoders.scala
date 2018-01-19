package ciris.decoders

import java.nio.file._

import ciris.ConfigDecoder

trait JavaNioFileConfigDecoders {
  implicit val pathConfigDecoder: ConfigDecoder[Path] =
    ConfigDecoder.catchNonFatal("Path")(Paths.get(_))
}
