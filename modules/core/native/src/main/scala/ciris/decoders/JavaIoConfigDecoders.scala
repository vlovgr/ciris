package ciris.decoders

import java.io.File

import ciris.ConfigDecoder

private[ciris] trait JavaIoConfigDecoders {
  implicit val fileConfigDecoder: ConfigDecoder[String, File] =
    ConfigDecoder.catchNonFatal("File")(new File(_))
}
