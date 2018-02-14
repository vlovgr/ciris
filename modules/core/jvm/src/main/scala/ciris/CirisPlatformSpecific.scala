package ciris

import ciris.api._

import java.io.File
import java.nio.charset.Charset

private[ciris] trait CirisPlatformSpecific {

  /**
    * Reads the contents of the specified `file` with `charset`,
    * applies the `modifyFileContents` function on the contents,
    * and attempts to convert the modified file contents to type
    * `Value`. The result is wrapped in a [[ConfigEntry]].<br>
    * <br>
    * Note that this function is not pure, and that there is an
    * alternative pure version of this function available as
    * [[fileSync]], which suspends the reading of the file.
    *
    * @param file the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @param decoder the decoder with which to decode the value
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
  )(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, (File, Charset), String, Value] = {
    ConfigSource.File
      .read((file, charset))
      .mapValue(modifyFileContents)
      .decodeValue[Value]
  }

  /**
    * Reads the contents of the specified `file` with `charset`,
    * applies the `modifyFileContents` function on the contents,
    * and attempts to convert the modified file contents to type
    * `Value`. The reading of the file is suspended into context
    * `F` synchronously.<br>
    * <br>
    * If suspending into a context is not desired, a non-pure version
    * of this function is available as [[file]], which attempts to
    * read the file immediately.
    *
    * @param file the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @param decoder the decoder with which to decode the value
    * @tparam F the context in which to suspend the reading
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @see [[fileWithNameSync]]
    */
  def fileSync[F[_]: Sync, Value](
    file: File,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, (File, Charset), String, Value] = {
    ConfigSource.File
      .suspendF[F]
      .read((file, charset))
      .mapValue(modifyFileContents)
      .decodeValue[Value]
  }

  /**
    * Reads the contents of the specified file `name` with `charset`,
    * applies the `modifyFileContents` function on the contents, and
    * attempts to convert the modified file contents to type `Value`.
    * The result is wrapped in a [[ConfigEntry]].<br>
    * <br>
    * Note that this function is not pure, and that there is an
    * alternative pure version of this function available as
    * [[fileWithNameSync]], which suspends the reading of
    * the file.
    *
    * @param name the name of the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @param decoder the decoder with which to decode the value
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
  )(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, (File, Charset), String, Value] = {
    this.file(new File(name), modifyFileContents, charset)
  }

  /**
    * Reads the contents of the specified file `name` with `charset`,
    * applies the `modifyFileContents` function on the contents, and
    * attempts to convert the modified file contents to type `Value`.
    * The reading of the file is suspended into context `F`.<br>
    * <br>
    * If suspending into a context is not desired, a non-pure version
    * of this function is available as [[fileWithName]], which attempts
    * to read the file immediately.
    *
    * @param name the name of the file of which to read the contents
    * @param modifyFileContents the function to apply on the file contents
    *                           before trying to convert to type `Value`;
    *                           defaults to not modify the file contents
    * @param charset the charset of the file to read;
    *                defaults to `Charset.defaultCharset`
    * @param decoder the decoder with which to decode the value
    * @tparam F the context in which to suspend the reading
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @see [[fileSync]]
    */
  def fileWithNameSync[F[_]: Sync, Value](
    name: String,
    modifyFileContents: String => String = identity,
    charset: Charset = Charset.defaultCharset
  )(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, (File, Charset), String, Value] = {
    this.fileSync[F, Value](new File(name), modifyFileContents, charset)
  }
}
