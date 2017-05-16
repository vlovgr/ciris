package ciris.readers

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

import scala.concurrent.duration.{Duration, FiniteDuration}

trait DurationConfigReaders {
  implicit val durationConfigReader: ConfigReader[Duration] =
    catchNonFatal("Duration")(Duration.apply)

  implicit val finiteDurationConfigReader: ConfigReader[FiniteDuration] =
    durationConfigReader.mapOption("FiniteDuration") { duration =>
      Some(duration).collect { case finite: FiniteDuration => finite }
    }
}
