import ciris.api._

import java.io.File
import java.nio.charset.Charset

/**
  * The main namespace of Ciris is `ciris`, and the easiest way to
  * get started is to bring it into scope with an import.<br>
  * If you are looking for a getting started guide, with examples
  * and explanations, please refer to the [[https://cir.is/docs usage guide]].
  */
package object ciris extends LoadConfigs {

  /**
    * Reads the environment variable with the specified key name,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigEntry]].<br>
    * <br>
    * If you want the value returned in a context, that is not
    * [[api.Id]], you can use [[envF]] and explicitly specify
    * the context to use.
    *
    * @param key the name of the environment variable to read
    * @param decoder the decoder with which to decode the value
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @example {{{
    * scala> ciris.env[Int]("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def env[Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, String, String, Value] = {
    envF[Id, Value](key)
  }

  /**
    * Reads the environment variable with the specified key name,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigEntry]] where the value has been
    * lifted into context `F`.<br>
    * <br>
    * If no context `F` is desired, [[api.Id]] can be used. There
    * is also [[env]] which can be used for the case when `F` is
    * [[api.Id]].
    *
    * @param key the name of the environment variable to read
    * @param decoder the decoder with which to decode the value
    * @tparam F the context in which to lift the value
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    */
  def envF[F[_]: Applicative, Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, String, String, Value] = {
    ConfigSource.Environment
      .read(key)
      .decodeValue[Value]
      .transformF[F]
  }

  /**
    * Reads the system property with the specified key name,
    * and tries to convert the value to type `Value`. Note
    * that system properties are mutable, so if you modify
    * them during runtime, this function is not pure.<br>
    * <br>
    * You might want to use [[propF]] to suspend the actual
    * reading in a context `F`, like `IO`.
    *
    * @param key the system property to read
    * @param decoder the decoder with which to decode the value
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @example {{{
    * scala> ciris.prop[Int]("key").toStringWithValues
    * res0: String = ConfigEntry(key, Property, Left(MissingKey(key, Property)))
    * }}}
    */
  def prop[Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, String, String, Value] = {
    ConfigSource.Properties
      .read(key)
      .decodeValue[Value]
  }

  /**
    * Reads the system property with the specified key name,
    * but suspends the reading by wrapping the operation in
    * the specified context `F`, and then tries to convert
    * the value to type `Value`.<br>
    * <br>
    * Note that system properties are mutable. If you never
    * modify them during runtime, and you are sure that you
    * do not want to suspend the reading to a context `F`,
    * you can instead use [[prop]].
    *
    * @param key the system property to read
    * @param decoder the decoder with which to decode the value
    * @tparam F the context in which to suspend the reading
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    */
  def propF[F[_]: Sync, Value](key: String)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, String, String, Value] = {
    ConfigSource.Properties
      .suspendF[F]
      .read(key)
      .decodeValue[Value]
  }

  /**
    * Reads the command-line argument with the specified index,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigEntry]].<br>
    * <br>
    * Note that the underlying arguments may be mutable. There
    * is also an alternative pure version [[argF]] with which
    * the reading is suspended into a context `F`.
    *
    * @param args the command-line arguments
    * @param index the index of the argument to read
    * @param decoder the decoder with which to decode the value
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    * @example {{{
    * scala> ciris.arg[Int](Array("50"))(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(50))
    *
    * scala> ciris.arg[Int](Array("50"))(1).toStringWithValues
    * res1: String = ConfigEntry(1, Argument, Left(MissingKey(1, Argument)))
    *
    * scala> ciris.arg[Int](Array("a"))(0).toStringWithValues
    * res2: String = ConfigEntry(0, Argument, Right(a), Left(WrongType(0, Argument, Right(a), a, Int, java.lang.NumberFormatException: For input string: "a")))
    * }}}
    */
  def arg[Value](args: IndexedSeq[String])(index: Int)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[Id, Int, String, Value] = {
    ConfigSource
      .byIndex(ConfigKeyType.Argument)(args)
      .read(index)
      .decodeValue[Value]
  }

  /**
    * Reads the command-line argument with the specified index,
    * and tries to convert the value to type `Value`. Note that
    * the underlying arguments may be mutable, so this function
    * suspends the reading into context `F`.<br>
    * <br>
    * If you're sure that the underlying arguments are never
    * modified at runtime, and do not want to suspend reading,
    * there is also [[arg]] which reads the argument directly.
    *
    * @param args the command-line arguments
    * @param index the index of the argument to read
    * @param decoder the decoder with which to decode the value
    * @tparam F the context in which to suspend the reading
    * @tparam Value the type to convert the value to
    * @return a [[ConfigEntry]] with the result
    */
  def argF[F[_]: Sync, Value](args: IndexedSeq[String])(index: Int)(
    implicit decoder: ConfigDecoder[String, Value]
  ): ConfigEntry[F, Int, String, Value] = {
    ConfigSource
      .byIndex(ConfigKeyType.Argument)(args)
      .suspendF[F]
      .read(index)
      .decodeValue[Value]
  }

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
