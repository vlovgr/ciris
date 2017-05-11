package ciris.readers

import java.nio.charset._

import ciris.ConfigReader

trait JavaNioCharsetConfigReaders {
  implicit val charsetConfigReader: ConfigReader[Charset] =
    ConfigReader.catchNonFatal("Charset")(Charset.forName)
}
