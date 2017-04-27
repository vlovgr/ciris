package ciris.readers

import java.nio.file.{Path, Paths}

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

trait JavaNioConfigReaders {
  implicit val pathConfigReader: ConfigReader[Path] =
    catchNonFatal("Path")(Paths.get(_))
}
