package ciris

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {

  /**
    * Reads the contents of the specified `file` with `charset`,
    * applies the `modifyFileContents` function on the contents,
    * and attempts to convert the modified file contents to type
    * `Value`. The result is wrapped in a [[ConfigValue]].
    *
    * @param file the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @see [[fileWithName]]
    * @example {{{
    * scala> file[Double](new java.io.File("/number.txt"))
    * res0: ConfigValue[Double] = ConfigValue(Left(ReadException((/number.txt,UTF-8), ConfigKeyType(file), java.io.FileNotFoundException: /number.txt (No such file or directory))))
    * }}}
    */
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

  /**
    * Reads the contents of the specified file `name` with `charset`,
    * applies the `modifyFileContents` function on the contents, and
    * attempts to convert the modified file contents to type `Value`.
    * The result is wrapped in a [[ConfigValue]].
    *
    * @param name the name of the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @see [[file]]
    * @example {{{
    * scala> fileWithName[Double]("/number.txt")
    * res0: ConfigValue[Double] = ConfigValue(Left(ReadException((/number.txt,UTF-8), ConfigKeyType(file), java.io.FileNotFoundException: /number.txt (No such file or directory))))
    * }}}
    */
  def fileWithName[Value: ConfigReader](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  ): ConfigValue[Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }
}
