package ciris.decoders

import java.util.UUID
import java.util.regex.Pattern

import ciris.ConfigDecoder

trait JavaUtilConfigDecoders {
  implicit val regexPatternConfigDecoder: ConfigDecoder[String, Pattern] =
    ConfigDecoder.catchNonFatal[String]("Pattern")(Pattern.compile)

  implicit val uuidConfigDecoder: ConfigDecoder[String, UUID] =
    ConfigDecoder.catchNonFatal[String]("UUID")(UUID.fromString)
}
