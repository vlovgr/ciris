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
    * the result in a [[ConfigEntry]].
    *
    * @param key the name of the environment variable to read
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
    ConfigSource.Environment
      .read(key)
      .decodeValue[Value]
  }

  /**
    * Reads the system property with the specified key name,
    * and tries to convert the value to type `Value`,
    * wrapping the result in a [[ConfigEntry]].
    *
    * @param key the system property to read
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
    * Reads the command-line argument with the specified index,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigEntry]].
    *
    * @param args the command-line arguments
    * @param index the index of the argument to read
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
}
