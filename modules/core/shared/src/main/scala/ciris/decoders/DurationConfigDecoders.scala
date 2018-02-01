package ciris.decoders

import ciris.ConfigDecoder

import scala.concurrent.duration.{Duration, FiniteDuration}

private[ciris] trait DurationConfigDecoders {
  implicit val durationConfigDecoder: ConfigDecoder[String, Duration] =
    ConfigDecoder.catchNonFatal("Duration")(Duration.apply)

  implicit val finiteDurationConfigDecoder: ConfigDecoder[String, FiniteDuration] =
    durationConfigDecoder.mapOption("FiniteDuration") { duration =>
      Some(duration).collect { case finite: FiniteDuration => finite }
    }
}
