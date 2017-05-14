package ciris.readers

import java.util.UUID
import java.util.regex.Pattern

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

trait JavaUtilConfigReaders {
  implicit val regexPatternConfigReader: ConfigReader[Pattern] =
    catchNonFatal("Pattern")(Pattern.compile)

  implicit val uuidConfigReader: ConfigReader[UUID] =
    catchNonFatal("UUID")(UUID.fromString)
}
