package ciris.readers

import java.util.UUID

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

trait JavaUtilConfigReaders {
  implicit val uuidConfigReader: ConfigReader[UUID] =
    catchNonFatal("UUID")(UUID.fromString)
}
