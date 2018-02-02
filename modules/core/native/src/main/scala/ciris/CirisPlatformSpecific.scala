package ciris

import ciris.api._

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {
  def file[Value](
    file: File,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(implicit decoder: ConfigDecoder[String, Value]): ConfigEntry[Id, (File, Charset), String, Value] = {
    ConfigSource.File
      .read((file, charset))
      .mapValue(modifyFileContents)
      .decodeValue[Value]
  }

  def fileWithName[Value](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(implicit decoder: ConfigDecoder[String, Value]): ConfigEntry[Id, (File, Charset), String, Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }
}
