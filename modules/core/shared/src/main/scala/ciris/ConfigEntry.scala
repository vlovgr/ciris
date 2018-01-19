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
    * Transforms the value of this [[ConfigEntry]] by applying the specified
    * function, returning a new [[ConfigEntry]] with the modified value. The
    * existing [[ConfigEntry]] and all other properties are left unmodified.
    *
    * @param f the function to apply to the value
    * @return a new [[ConfigEntry]] with the modified value
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value ))
    *
    * scala> entry.mapValue(_.right.map(_.trim))
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    *
    * scala> entry.mapValueRight(_.trim)
    * res1: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    * }}}
    * @see [[mapValueRight]]
    */
  def mapValue[A](f: Either[ConfigError, V] => Either[ConfigError, A]): ConfigEntry[K, S, A] =
    new ConfigEntry(key, keyType, sourceValue, f(value))

  /**
    * Transforms the value of this [[ConfigEntry]] if it is available, by
    * applying the specified function, returning a new [[ConfigEntry]]
    * with the modified value. The existing [[ConfigEntry]] and all
    * other properties are left unmodified.
    *
    * @param f the function to apply to the value
    * @return a new [[ConfigEntry]] with the modified value;
    *         or a [[ConfigError]] if the value is not available
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ))
    *
    * scala> entry.mapValueRight(_.trim)
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Right(value ), Right(value))
    * }}}
    */
  def mapValueRight[A](f: V => A): ConfigEntry[K, S, A] =
    mapValue(_.right.map(f))

  override def toString: String = {
    if(sourceValue == value) s"ConfigEntry($key, $keyType, $value)"
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
