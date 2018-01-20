package ciris

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {
  def file[Value](
    file: File,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(implicit decoder: ConfigDecoder[String, Value]): ConfigValue[Value] = {
    ConfigValue((file, charset))(
      ConfigSource.File,
      decoder.mapEntryValue(modifyFileContents)
    )
  }

  def fileWithName[Value](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(implicit decoder: ConfigDecoder[String, Value]): ConfigValue[Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }
}
