package ciris.readers

import java.net.{URI, URL}

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

trait JavaNetConfigReaders {
  implicit val uriConfigReader: ConfigReader[URI] =
    catchNonFatal("URI")(new URI(_))

  implicit val urlConfigReader: ConfigReader[URL] =
    catchNonFatal("URL")(new URL(_))
}
