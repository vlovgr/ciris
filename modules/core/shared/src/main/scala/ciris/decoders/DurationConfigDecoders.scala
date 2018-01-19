package ciris.decoders

import ciris.ConfigDecoder
import ciris.ConfigDecoder.catchNonFatal

import scala.concurrent.duration.{Duration, FiniteDuration}

trait DurationConfigDecoders {
  implicit val durationConfigDecoder: ConfigDecoder[Duration] =
    catchNonFatal("Duration")(Duration.apply)

  implicit val finiteDurationConfigDecoder: ConfigDecoder[FiniteDuration] =
    durationConfigDecoder.mapOption("FiniteDuration") { duration =>
      Some(duration).collect { case finite: FiniteDuration => finite }
    }
}
