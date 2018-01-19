package ciris.decoders

import java.util.UUID
import java.util.regex.Pattern

import ciris.ConfigDecoder
import ciris.ConfigDecoder.catchNonFatal

trait JavaUtilConfigDecoders {
  implicit val regexPatternConfigDecoder: ConfigDecoder[Pattern] =
    catchNonFatal("Pattern")(Pattern.compile)

  implicit val uuidConfigDecoder: ConfigDecoder[UUID] =
    catchNonFatal("UUID")(UUID.fromString)
}
