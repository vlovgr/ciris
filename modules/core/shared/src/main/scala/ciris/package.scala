/**
  * The main namespace of Ciris is `ciris`, and the easiest way to
  * get started is to bring it into scope with an import.<br>
  * If you are looking for a getting started guide, with examples and explanations, please refer to the [[https://cir.is/docs/basics usage guide]].
  */
package object ciris extends LoadConfigs with CirisPlatformSpecific {

  /**
    * Reads the environment variable with the specified key name,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigValue]].
    *
    * @param key the name of the environment variable to read
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @example {{{
    * scala> ciris.env[Int]("key")
    * res0: ciris.ConfigValue[Int] = ConfigValue(Left(MissingKey(key, Environment)))
    * }}}
    */
  def env[Value: ConfigReader](key: String): ConfigValue[Value] =
    ConfigValue(key)(ConfigSource.Environment, ConfigReader[Value])

  /**
    * Reads the system property with the specified key name,
    * and tries to convert the value to type `Value`,
    * wrapping the result in a [[ConfigValue]].
    *
    * @param key the system property to read
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @example {{{
    * scala> ciris.prop[Int]("key")
    * res0: ciris.ConfigValue[Int] = ConfigValue(Left(MissingKey(key, Property)))
    * }}}
    */
  def prop[Value: ConfigReader](key: String): ConfigValue[Value] =
    ConfigValue(key)(ConfigSource.Properties, ConfigReader[Value])

  /**
    * Reads the command-line argument with the specified index,
    * and tries to convert the value to type `Value`, wrapping
    * the result in a [[ConfigValue]].
    *
    * @param args the command-line arguments
    * @param index the index of the argument to read
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @example {{{
    * scala> ciris.arg[Int](Array("50"))(0)
    * res0: ciris.ConfigValue[Int] = ConfigValue(Right(50))
    *
    * scala> ciris.arg[Int](Array("50"))(1)
    * res1: ciris.ConfigValue[Int] = ConfigValue(Left(MissingKey(1, Argument)))
    *
    * scala> ciris.arg[Int](Array("a"))(0)
    * res2: ciris.ConfigValue[Int] = ConfigValue(Left(WrongType(0, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a"))))
    * }}}
    */
  def arg[Value: ConfigReader](args: IndexedSeq[String])(index: Int): ConfigValue[Value] =
    ConfigValue(index)(ConfigSource.byIndex(ConfigKeyType.Argument)(args), ConfigReader[Value])

  /**
    * Reads from an implicit source, and tries to convert the value
    * to type `Value`, wrapping the result in a [[ConfigValue]].
    *
    * @tparam Value the type to convert the value to
    * @return a [[ConfigValue]] with the result
    * @note this method uses partial type application via the intermediate
    *       class [[ConfigValue.PartiallyApplied]]. You do not really need
    *       to know anything about it, except how its used, so see the example.
    * @example {{{
    * scala> implicit val source = ciris.ConfigSource.Environment
    * source: ciris.ConfigSource.Environment.type = Environment
    *
    * scala> ciris.read[Int]("key")
    * res0: ciris.ConfigValue[Int] = ConfigValue(Left(MissingKey(key, Environment)))
    * }}}
    */
  def read[Value]: ConfigValue.PartiallyApplied[Value] =
    new ConfigValue.PartiallyApplied[Value]
}
