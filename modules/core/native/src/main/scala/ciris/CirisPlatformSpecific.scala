package ciris

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {
  def file[Value: ConfigReader](
    file: File,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  ): ConfigValue[Value] = {
    ConfigValue((file, charset))(
      ConfigSource.File,
      ConfigReader[Value]
        .mapEntryValue(modifyFileContents)
    )
  }

  def fileWithName[Value: ConfigReader](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  ): ConfigValue[Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }
}
