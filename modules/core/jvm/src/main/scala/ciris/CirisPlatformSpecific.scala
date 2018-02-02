package ciris

import ciris.api._

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {

  /**
    * Reads the contents of the specified `file` with `charset`,
    * applies the `modifyFileContents` function on the contents,
    * and attempts to convert the modified file contents to type
    * `Value`. The result is wrapped in a [[ConfigEntry]].
    *
    * @param file the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @see [[fileWithName]]
    * @example {{{
    * scala> file[Double](new java.io.File("/number.txt"))
    * res0: ConfigEntry[api.Id, (java.io.File,java.nio.charset.Charset), String, Double] = ConfigEntry((/number.txt,UTF-8), ConfigKeyType(file), Left(ReadException((/number.txt,UTF-8), ConfigKeyType(file), java.io.FileNotFoundException: /number.txt (No such file or directory))))
    * }}}
    */
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

  /**
    * Reads the contents of the specified file `name` with `charset`,
    * applies the `modifyFileContents` function on the contents, and
    * attempts to convert the modified file contents to type `Value`.
    * The result is wrapped in a [[ConfigEntry]].
    *
    * @param name the name of the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @see [[file]]
    * @example {{{
    * scala> fileWithName[Double]("/number.txt")
    * res0: ConfigEntry[api.Id, (java.io.File,java.nio.charset.Charset), String, Double] = ConfigEntry((/number.txt,UTF-8), ConfigKeyType(file), Left(ReadException((/number.txt,UTF-8), ConfigKeyType(file), java.io.FileNotFoundException: /number.txt (No such file or directory))))
    * }}}
    */
  def fileWithName[Value](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(implicit decoder: ConfigDecoder[String, Value]): ConfigEntry[Id, (File, Charset), String, Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }
}
