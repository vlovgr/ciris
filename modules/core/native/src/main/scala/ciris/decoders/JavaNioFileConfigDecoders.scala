package ciris.decoders

import java.nio.file._

import ciris.ConfigDecoder

trait JavaNioFileConfigDecoders {
  implicit val pathConfigDecoder: ConfigDecoder[String, Path] =
    ConfigDecoder.catchNonFatal[String]("Path")(Paths.get(_))
}
