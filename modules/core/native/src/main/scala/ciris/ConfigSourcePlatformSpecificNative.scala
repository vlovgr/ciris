package ciris

import ciris.api._

import java.io.{File => JFile}
import java.nio.charset.Charset

import scala.io.Source

private[ciris] trait ConfigSourcePlatformSpecific {
  object File extends ConfigSource[Id, (JFile, Charset), String](ConfigKeyType.File) {
    private val delegate: ConfigSource[Id, (JFile, Charset), String] =
      ConfigSource.catchNonFatal(keyType) {
        case (file, charset) =>
          Source.fromFile(file, charset.name).mkString
      }

    override def read(key: (JFile, Charset)): ConfigEntry[Id, (JFile, Charset), String, String] =
      delegate.read(key)

    override def toString: String =
      "File"
  }
}
