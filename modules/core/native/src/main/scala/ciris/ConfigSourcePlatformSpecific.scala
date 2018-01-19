package ciris

import java.io.{File => JFile}
import java.nio.charset.Charset

import scala.io.Source

private[ciris] trait ConfigSourcePlatformSpecific {
  case object File extends ConfigSource[(JFile, Charset)](ConfigKeyType.File) {
    private val delegate: ConfigSource[(JFile, Charset)] =
      ConfigSource.catchNonFatal(keyType) {
        case (file, charset) =>
          Source.fromFile(file, charset.name).mkString
      }

    override def read(key: (JFile, Charset)): ConfigSourceEntry[(JFile, Charset), String] =
      delegate.read(key)
  }
}
