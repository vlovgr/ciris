package ciris.readers

import java.io.File

import ciris.ConfigReader

trait JavaIoConfigReaders {
  implicit val fileConfigReader: ConfigReader[File] =
    ConfigReader.catchNonFatal("File")(new File(_))
}
