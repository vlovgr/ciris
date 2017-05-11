package ciris.readers

import java.nio.file._

import ciris.ConfigReader

trait JavaNioFileConfigReaders {
  implicit val pathConfigReader: ConfigReader[Path] =
    ConfigReader.catchNonFatal("Path")(Paths.get(_))
}
