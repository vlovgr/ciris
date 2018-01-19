package ciris

/**
  * A value which has been read from a [[ConfigSource]], and converted
  * to the specified type `Value` using a [[ConfigDecoder]]. The class
  * is a thin wrapper around an `Either[ConfigError, Value]` value to
  * support error accumulation.
  *
  * To create a [[ConfigValue]], you typically use methods like [[env]],
  * [[prop]], or [[read]], but if you need to write a similar method for
  * a custom configuration source, you can use the functions provided in
  * the companion object.
  *
  * {{{
  * scala> ConfigValue("key")(ConfigSource.Properties, ConfigDecoder[String])
  * res0: ConfigValue[String] = ConfigValue(Left(MissingKey(key, Property)))
  *
  * scala> ConfigValue(Right(123))
  * res1: ConfigValue[Int] = ConfigValue(Right(123))
  * }}}
  *
  * @tparam Value the type of the value
  */
final case class ConfigValue[Value](value: Either[ConfigError, Value]) extends AnyVal {

  /**
    * Returns a new [[ConfigValue]] with the value transformed using
    * the provided function. If there is no value, due to an error,
    * returns a [[ConfigValue]] with the error instead.
    *
    * @param f a function with which to transform the value
    * @tparam A the type of value the provided function returns
    * @return a new [[ConfigValue]] with the transformed value
    */
  final def map[A](f: Value => A): ConfigValue[A] =
    ConfigValue(value.right.map(f))

  /**
    * Returns a new [[ConfigValue]] from the existing value of this
    * [[ConfigValue]] and by applying the function `f`. If there is
    * no value, due to an error, returns a [[ConfigValue]] with the
    * error instead.
    *
    * @param f a function from which to create a new [[ConfigValue]]
    * @tparam A the type of value for the new [[ConfigValue]]
    * @return a new [[ConfigValue]] from the provided function
    */
  final def flatMap[A](f: Value => Either[ConfigError, A]): ConfigValue[A] =
    ConfigValue(value.right.flatMap(f))

  /**
    * If `this` configuration value was read successfully, uses `this`
    * value, otherwise uses the configuration value of `that`. If an
    * error occurred for both configuration values, their errors
    * will be accumulated.
    *
    * Note that the provided [[ConfigValue]] will only be evaluated
    * if `this` configuration value is an error. This allows you to
    * chain configuration values like in the following example.
    *
    * @param that the [[ConfigValue]] to use if `this` value is an error
    * @tparam A the type of `that` [[ConfigValue]] to use
    * @return a new [[ConfigValue]]
    * @example {{{
    * scala> val error =
    *      |  ConfigValue[Int](Left(ConfigError("error1"))).
    *      |    orElse(ConfigValue(Left(ConfigError("error2"))))
    * error: ConfigValue[Int] = ConfigValue(Left(Combined(Vector(ConfigError(error1), ConfigError(error2)))))
    *
    * scala> error.value.left.map(_.message).toString
    * res0: String = Left(error1, error2)
    *
    * scala> ConfigValue[Int](Left(ConfigError("error1"))).
    *      |   orElse(ConfigValue(Right(123)))
    * res1: ConfigValue[Int] = ConfigValue(Right(123))
    * }}}
    */
  def orElse[A >: Value](that: => ConfigValue[A]): ConfigValue[A] =
    ConfigValue(value.fold(thisError => {
      that.value.fold(
        thatError => Left(thisError combine thatError),
        thatValue => Right(thatValue)
      )
    }, thisValue => Right(thisValue)))

  private[ciris] def append[A](next: ConfigValue[A]): ConfigValue2[Value, A] = {
    (value, next.value) match {
      case (Right(a), Right(b))         => new ConfigValue2(Right((a, b)))
      case (Left(error1), Right(_))     => new ConfigValue2(Left(ConfigErrors(error1)))
      case (Right(_), Left(error2))     => new ConfigValue2(Left(ConfigErrors(error2)))
      case (Left(error1), Left(error2)) => new ConfigValue2(Left(error1 append error2))
    }
  }

  override def toString: String =
    s"ConfigValue($value)"
}

object ConfigValue {

  /**
    * Creates a new [[ConfigValue]] by reading the specified key from
    * the configuration source, converting the value to type `Value`
    * using the specified [[ConfigDecoder]].
    *
    * @param key the key to read
    * @param source the configuration source to read from
    * @param decoder the decoder to use to convert the value
    * @tparam Key the type of the key
    * @tparam Value the type of the value
    * @return a new [[ConfigValue]] with the read value
    * @example {{{
    * scala> ConfigValue("key")(ConfigSource.Properties, ConfigDecoder[String])
    * res0: ConfigValue[String] = ConfigValue(Left(MissingKey(key, Property)))
    * }}}
    */
  def apply[Key, Value](key: Key)(
    source: ConfigSource[Key],
    decoder: ConfigDecoder[Value]
  ): ConfigValue[Value] = {
    ConfigValue(decoder.decode[Key, String](source.read(key)))
  }

  /**
    * Partial type application of [[ConfigValue]] by first fixing
    * the `Value` type, allowing for type inference of the key's
    * type, while looking for an implicit [[ConfigSource]].
    *
    * This is to support syntax like the one for [[read]].
    *
    * {{{
    * scala> implicit val source = ConfigSource.Environment
    * source: ConfigSource.Environment.type = Environment
    *
    * scala> read[Int]("key")
    * res0: ConfigValue[Int] = ConfigValue(Left(MissingKey(key, Environment)))
    * }}}
    *
    * @tparam Value the type of value to read
    */
  final class PartiallyApplied[Value] {

    /**
      * Creates a [[ConfigValue]] by looking for an implicit [[ConfigSource]]
      * matching the specified key's type `Key`. Requires a [[ConfigDecoder]]
      * for `Value` to be able to read values.
      *
      * Typically, you would use this method via the [[read]] method.
      *
      * {{{
      * scala> implicit val source = ConfigSource.Environment
      * source: ConfigSource.Environment.type = Environment
      *
      * scala> read[Int]("key")
      * res0: ConfigValue[Int] = ConfigValue(Left(MissingKey(key, Environment)))
      * }}}
      *
      * @param key the key to read from the configuration source
      * @param source the configuration source from which to read
      * @param decoder the decoder to convert the value
      * @tparam Key the type of the key
      * @return a new [[ConfigValue]] with the value
      */
    def apply[Key](key: Key)(
      implicit source: ConfigSource[Key],
      decoder: ConfigDecoder[Value]
    ): ConfigValue[Value] = {
      ConfigValue[Key, Value](key)(source, decoder)
    }
  }
}
