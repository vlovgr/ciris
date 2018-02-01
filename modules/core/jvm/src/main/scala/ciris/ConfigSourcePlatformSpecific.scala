package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

import scala.io.Source

private[ciris] trait ConfigSourcePlatformSpecific {

  /**
    * [[ConfigSource]] reading file contents from `File` and `Charset` keys.
    */
  object File extends ConfigSource[(JFile, Charset), String](ConfigKeyType.File) {
    private val delegate: ConfigSource[(JFile, Charset), String] =
      ConfigSource.catchNonFatal(keyType) {
        case (file, charset) =>
          Source.fromFile(file, charset.name).mkString
      }

    override def read(key: (JFile, Charset)): ConfigEntry[(JFile, Charset), String, String] =
      delegate.read(key)

    override def toString: String =
      "File"
  }
}
