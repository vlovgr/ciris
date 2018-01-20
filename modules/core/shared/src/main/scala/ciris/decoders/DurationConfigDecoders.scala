package ciris.decoders

import ciris.ConfigDecoder

import scala.concurrent.duration.{Duration, FiniteDuration}

trait DurationConfigDecoders {
  implicit val durationConfigDecoder: ConfigDecoder[String, Duration] =
    ConfigDecoder.catchNonFatal[String]("Duration")(Duration.apply)

  implicit val finiteDurationConfigDecoder: ConfigDecoder[String, FiniteDuration] =
    durationConfigDecoder.mapOption("FiniteDuration") { duration =>
      Some(duration).collect { case finite: FiniteDuration => finite }
    }
}
