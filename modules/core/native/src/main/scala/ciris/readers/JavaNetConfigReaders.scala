package ciris.readers

import java.net._

import ciris.ConfigReader
import ciris.ConfigReader.catchNonFatal

trait JavaNetConfigReaders {
  implicit val inetAddressConfigReader: ConfigReader[InetAddress] =
    catchNonFatal("InetAddress")(InetAddress.getByName)

  implicit val uriConfigReader: ConfigReader[URI] =
    catchNonFatal("URI")(new URI(_))
}
