package ciris.decoders

import ciris.ConfigDecoder

import scala.util.matching.Regex

trait ScalaUtilConfigDecoders {
  implicit val regexConfigDecoder: ConfigDecoder[String, Regex] =
    ConfigDecoder.catchNonFatal("Regex")(new Regex(_))
}
