import ciris.api._

/**
  * The main namespace of Ciris is `ciris`, and the easiest way to
  * get started is to bring it into scope with an import.<br>
  * If you are looking for a getting started guide, with examples
  * and explanations, please refer to the [[https://cir.is/docs usage guide]].
  */
package object ciris extends LoadConfigs with CirisPlatformSpecific {

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
    * scala> ciris.env[Int]("key")
    * res0: ciris.ConfigEntry[ciris.api.Id, String, String, Int] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
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
    * scala> ciris.prop[Int]("key")
    * res0: ciris.ConfigEntry[ciris.api.Id, String, String, Int] = ConfigEntry(key, Property, Left(MissingKey(key, Property)))
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
    * scala> ciris.arg[Int](Array("50"))(0)
    * res0: ciris.ConfigEntry[ciris.api.Id, Int, String, Int] = ConfigEntry(0, Argument, Right(50))
    *
    * scala> ciris.arg[Int](Array("50"))(1)
    * res1: ciris.ConfigEntry[ciris.api.Id, Int, String, Int] = ConfigEntry(1, Argument, Left(MissingKey(1, Argument)))
    *
    * scala> ciris.arg[Int](Array("a"))(0)
    * res2: ciris.ConfigEntry[ciris.api.Id, Int, String, Int] = ConfigEntry(0, Argument, Right(a), Left(WrongType(0, Argument, Right(a), a, Int, java.lang.NumberFormatException: For input string: "a")))
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
}
