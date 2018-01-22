package ciris

/**
  * [[ConfigEntry]] represents an entry (key-value pair) from a configuration
  * source, with support for transforming the value. The value might not have
  * been retrieved or transformed successfully, represented by wrapping the
  * value in `Either[ConfigError, V]`. The key is of type `K` and the value
  * is of type `V`. The original value from the source is left unmodified
  * and is of type `S`. The type of the key is described with the
  * included [[ConfigKeyType]].<br>
  * <br>
  * To create a [[ConfigEntry]], use [[ConfigEntry#apply]].
  * {{{
  * scala> ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
  * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value))
  * }}}
  *
  * @param key the key which was retrieved from the configuration source
  * @param keyType the type of keys which the configuration source supports
  * @param sourceValue the original unmodified value from the configuration source
  * @param value the source value with zero or more transformations applied to it
  * @tparam K the type of the key
  * @tparam S the type of the source value
  * @tparam V the type of the value
  */
final class ConfigEntry[K, S, V] private (
  val key: K,
  val keyType: ConfigKeyType[K],
  val sourceValue: Either[ConfigError, S],
  val value: Either[ConfigError, V]
) {

  /**
    * Transforms the value of this [[ConfigEntry]] into type `A`, by
    * finding an implicit [[ConfigDecoder]] instance from `V` to `A`,
    * returning a new [[ConfigEntry]] with the modified value. The
    * existing entry and all other properties are left unmodified.
    *
    * @param decoder the implicit [[ConfigDecoder]] instance
    * @tparam A the type to which the value should be decoded
    * @return a new [[ConfigEntry]] with the modified value
    */
  def decodeValue[A](implicit decoder: ConfigDecoder[V, A]): ConfigEntry[K, S, A] =
    withValue(decoder.decode(this))

  /**
    * Transforms the value of this [[ConfigEntry]] if it is available, by
    * applying the specified function, returning a new [[ConfigEntry]]
    * with the modified value. The existing [[ConfigEntry]] and all
    * other properties are left unmodified.
    *
    * @param f the function to apply to the value
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]] with the modified value;
    *         or a [[ConfigError]] if the value is not available
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ))
    *
    * scala> entry.mapValue(_.trim)
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    * }}}
    */
  def mapValue[A](f: V => A): ConfigEntry[K, S, A] =
    transformValue(_.right.map(f))

  /**
    * Replaces the value of this [[ConfigEntry]] if it is available, by
    * applying the specified function on the existing value, returning a
    * new [[ConfigEntry]] with the new value. The existing [[ConfigEntry]]
    * and all other properties are left unmodified.
    *
    * @param f the function to apply to the value, if it is available
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]] with the new value;
    *         or a [[ConfigError]] if the value is not available
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value))
    *
    * scala> entry.flatMapValue(v => if(v.length > 2) Right(v) else Left(ConfigError("error")))
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value))
    * }}}
    */
  def flatMapValue[A](f: V => Either[ConfigError, A]): ConfigEntry[K, S, A] =
    transformValue(_.right.flatMap(f))

  /**
    * Replaces the value of this [[ConfigEntry]] with the specified value,
    * by returning a new [[ConfigEntry]] with the new value. The existing
    * [[ConfigEntry]] and all other properties are left unmodified.
    *
    * @param value the value to replace the existing one
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]] with the new value
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value))
    *
    * scala> entry.withValue(Right(123))
    * res0: ConfigEntry[String, String, Int] = ConfigEntry(key, Environment, Right(value), Right(123))
    * }}}
    */
  def withValue[A](value: Either[ConfigError, A]): ConfigEntry[K, S, A] =
    transformValue(_ => value)

  /**
    * Replaces the value of this [[ConfigEntry]] by applying the specified
    * function to the existing value, returning a new [[ConfigEntry]] with
    * the new value. The existing [[ConfigEntry]] and all other properties
    * are left unmodified.
    *
    * @param f the function to apply to the value
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]] with the new value
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value ))
    *
    * scala> entry.transformValue(_.right.map(_.trim))
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    *
    * scala> entry.mapValue(_.trim)
    * res1: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    * }}}
    */
  def transformValue[A](f: Either[ConfigError, V] => Either[ConfigError, A]): ConfigEntry[K, S, A] =
    new ConfigEntry(key, keyType, sourceValue, f(value))

  /**
    * If the value of this [[ConfigEntry]] is unavailable, tries to
    * use the value of that [[ConfigEntry]], accumulating errors if
    * both values are unavailable. Returns a new [[ConfigEntry]],
    * and the existing [[ConfigEntry]] and all other properties
    * are left unmodified.
    *
    * Note that the provided [[ConfigEntry]] will only be evaluated
    * if the value of this [[ConfigEntry]] is unavailable. This
    * allows you to chain [[ConfigEntry]]s like in the example.
    *
    * @param that the [[ConfigEntry]] to use if this value is unavailable
    * @tparam A the value type of that [[ConfigEntry]]
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> val entry =
    *      |  ConfigEntry[String, Int]("key", ConfigKeyType.Environment, Left(ConfigError("error1"))).
    *      |    orElse(ConfigEntry[String, Int]("key2", ConfigKeyType.Property, Left(ConfigError("error2"))))
    * error: ConfigEntry[String, Int, Int] = ConfigEntry(key, Environment, Left(ConfigError("error1"), Left(Combined(Vector(ConfigError(error1), ConfigError(error2)))))
    *
    * scala> entry.value.left.map(_.message).toString
    * res0: String = Left(error1, error2)
    *
    * scala> ConfigEntry[String, Int]("key", ConfigKeyType.Environment, Left(ConfigError("error1"))).
    *      |   orElse(ConfigEntry("key2", ConfigKeyType.Property, Right(123)))
    * res1: ConfigEntry[String, Int, Int] = ConfigEntry(key, Environment, Left(ConfigError(error1)), Right(123))
    * }}}
    */
  def orElse[A >: V](that: => ConfigEntry[_, _, A]): ConfigEntry[K, S, A] =
    transformValue(_.fold(thisError => {
      that.value.fold(
        thatError => Left(thisError combine thatError),
        thatValue => Right(thatValue)
      )
    }, thisValue => Right(thisValue)))

  private[ciris] def append[A](next: ConfigEntry[_, _, A]): ConfigValue2[V, A] = {
    (value, next.value) match {
      case (Right(v), Right(a))         => new ConfigValue2(Right((v, a)))
      case (Left(error1), Right(_))     => new ConfigValue2(Left(ConfigErrors(error1)))
      case (Right(_), Left(error2))     => new ConfigValue2(Left(ConfigErrors(error2)))
      case (Left(error1), Left(error2)) => new ConfigValue2(Left(error1 append error2))
    }
  }

  override def toString: String = {
    val sourceValueString = sourceValue.toString
    val valueString = value.toString

    if (sourceValueString == valueString) s"ConfigEntry($key, $keyType, $value)"
    else s"ConfigEntry($key, $keyType, $sourceValue, $value)"
  }
}

object ConfigEntry {

  /**
    * Creates a new [[ConfigEntry]] representing an entry (key-value pair) from a
    * configuration source. The value might not have been retrieved successfully,
    * represented by wrapping the value in `Either[ConfigError, S]`. The key is
    * of type `K` and the source value is of type `S`. The type of the key is
    * described with the [[ConfigKeyType]].
    *
    * @param key the key which was retrieved from the configuration source
    * @param keyType the type of keys which the configuration source supports
    * @param sourceValue the value for the key from the configuration source
    * @tparam K the type of the key
    * @tparam S the type of the source value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value))
    * }}}
    */
  def apply[K, S](
    key: K,
    keyType: ConfigKeyType[K],
    sourceValue: Either[ConfigError, S]
  ): ConfigEntry[K, S, S] = {
    new ConfigEntry(key, keyType, sourceValue, sourceValue)
  }
}
