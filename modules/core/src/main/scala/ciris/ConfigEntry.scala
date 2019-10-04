package ciris

import cats.{~>, Apply, Id, Monad, Show}
import cats.implicits._

/**
  * [[ConfigEntry]] represents an entry (key-value pair) from a configuration
  * source, with support for transforming the value. The value might not have
  * been retrieved or transformed successfully, represented by wrapping the
  * value in `Either[ConfigError, V]`. The key is of type `K` and the value
  * is of type `V`. The original value from the source is left unmodified
  * and is of type `S`. Both the source value and the value are in a
  * context of type `F`. The type of the key is described with the
  * included [[ConfigKeyType]].<br>
  * <br>
  * To create a [[ConfigEntry]], use [[ConfigEntry#apply]].
  * {{{
  * scala> ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
  * res0: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
  * }}}
  *
  * @param key the key which was retrieved from the configuration source
  * @param keyType the type of keys which the configuration source supports
  * @param sourceValue the original unmodified value from the configuration source
  * @param value the source value with zero or more transformations applied to it
  * @tparam F the context in which the values exists
  * @tparam K the type of the key
  * @tparam S the type of the source value
  * @tparam V the type of the value
  */
final class ConfigEntry[F[_]: Apply, K, S, V] private (
  val key: K,
  val keyType: ConfigKeyType[K],
  val sourceValue: F[Either[ConfigError, S]],
  val value: F[Either[ConfigError, V]]
) extends ConfigValue[F, V] {

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
  def decodeValue[A](
    implicit decoder: ConfigDecoder[V, A],
    monad: Monad[F]
  ): ConfigEntry[F, K, S, A] = {
    new ConfigEntry(key, keyType, sourceValue, decoder.decode(this))
  }

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
    * entry: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> entry.mapValue(_.trim).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value ), Right(value))
    *
    * }}}
    */
  def mapValue[A](f: V => A): ConfigEntry[F, K, S, A] =
    transformValue(_.map(f))

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
    * entry: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> entry.flatMapValue(v => if(v.length > 2) Right(v) else Left(ConfigError("error"))).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value))
    * }}}
    */
  def flatMapValue[A](f: V => Either[ConfigError, A]): ConfigEntry[F, K, S, A] =
    transformValue(_.flatMap(f))

  /**
    * Replaces the value of this [[ConfigEntry]] with the specified value,
    * by returning a new [[ConfigEntry]] with the new value. The existing
    * [[ConfigEntry]] and all other properties are left unmodified.
    *
    * @param value the value to replace the existing one
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * entry: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> entry.withValue(Right(123)).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value), Right(123))
    * }}}
    */
  def withValue[A](value: Either[ConfigError, A]): ConfigEntry[F, K, S, A] =
    transformValue(_ => value)

  /**
    * Replaces the value of this [[ConfigEntry]] with the specified value,
    * in the same type of context `F`. This function returns a new entry
    * with the specified value. The existing [[ConfigEntry]] and all
    * other properties ar left unmodified.
    *
    * @param value the value to replace the existing one
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * entry: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> entry.withValueF(Right(123)).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value), Right(123))
    *
    * scala> entry.withValue(Right(456)).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value), Right(456))
    * }}}
    */
  def withValueF[A](value: F[Either[ConfigError, A]]): ConfigEntry[F, K, S, A] =
    new ConfigEntry(key, keyType, sourceValue, value)

  /**
    * Replaces the value of this [[ConfigEntry]] by applying the specified
    * function to the existing value, returning a new [[ConfigEntry]] with
    * the new value. The existing [[ConfigEntry]] and all other properties
    * are left unmodified.
    *
    * @param f the function to apply to the value
    * @tparam A the type of the new value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> val entry = ConfigEntry("key", ConfigKeyType.Environment, Right("value "))
    * entry: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> entry.transformValue(_.map(_.trim)).toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Right(value ), Right(value))
    *
    * scala> entry.mapValue(_.trim).toStringWithValues
    * res1: String = ConfigEntry(key, Environment, Right(value ), Right(value))
    * }}}
    */
  def transformValue[A](
    f: Either[ConfigError, V] => Either[ConfigError, A]
  ): ConfigEntry[F, K, S, A] = {
    new ConfigEntry(key, keyType, sourceValue, value.map(f))
  }

  /**
    * Transforms the context `F` for the source value and value
    * of this [[ConfigEntry]] into another context `G`.
    *
    * @param f the natural transformation from `F` to `G`
    * @tparam G the context to which `F` should be transformed
    * @return a new [[ConfigEntry]]
    */
  def transformF[G[_]](f: F ~> G)(implicit G: Apply[G]): ConfigEntry[G, K, S, V] =
    new ConfigEntry(key, keyType, f(sourceValue), f(value))

  override def toString: String =
    s"ConfigEntry($key, $keyType)"

  /**
    * Returns a `String` representation of this [[ConfigEntry]]
    * including the value. If the value is potentially sensitive,
    * then be careful to not include it in log output.
    *
    * @return a `String` representation with the value
    */
  override def toStringWithValue: String =
    s"ConfigEntry($key, $keyType, $value)"

  /**
    * Returns a `String` representation of this [[ConfigEntry]]
    * including both the source value and value. If the values
    * include potentially sensitive details, be careful to
    * not include them in log output.
    *
    * @return a `String` representation with values
    */
  def toStringWithValues: String = {
    val sourceValueString = sourceValue.toString
    val valueString = value.toString

    if (sourceValueString == valueString) s"ConfigEntry($key, $keyType, $valueString)"
    else s"ConfigEntry($key, $keyType, $sourceValueString, $valueString)"
  }

  override def toStringWithResult: String =
    s"ConfigEntry($key, $keyType, $result)"
}

object ConfigEntry {

  /**
    * Creates a new [[ConfigEntry]] representing an entry (key-value pair) from a
    * configuration source. The value might not have been retrieved successfully,
    * represented by wrapping the value in `Either[ConfigError, S]`. The key is
    * of type `K` and the source value is of type `S`. The type of the key is
    * described with the [[ConfigKeyType]].<br>
    * <br>
    * If the source value is in a context `F`, [[ConfigEntry#applyF]] can instead
    * be used to create a [[ConfigEntry]] with the value.
    *
    * @param key the key which was retrieved from the configuration source
    * @param keyType the type of keys which the configuration source supports
    * @param sourceValue the value for the key from the configuration source
    * @tparam K the type of the key
    * @tparam S the type of the source value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * res0: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> ConfigEntry.applyF[cats.Id, String, String]("key", ConfigKeyType.Environment, Right("value"))
    * res1: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    * }}}
    */
  def apply[K, S](
    key: K,
    keyType: ConfigKeyType[K],
    sourceValue: Either[ConfigError, S]
  ): ConfigEntry[Id, K, S, S] = {
    ConfigEntry.applyF[Id, K, S](key, keyType, sourceValue)
  }

  /**
    * Creates a new [[ConfigEntry]] representing an entry (key-value pair) from a
    * configuration source. The value might not have been retrieved successfully,
    * represented by wrapping the value in `Either[ConfigError, S]`. The key is
    * of type `K` and the source value is of type `S`. The source value is in
    * a context of type `F`. The type of the key is described with the
    * specified [[ConfigKeyType]].<br>
    * <br>
    * If no context `F` is desired, `Id` can be used. There is also a
    * convenience function [[ConfigEntry#apply]], which creates entries with
    * `F` set to `Id`.
    *
    * @param key the key which was retrieved from the configuration source
    * @param keyType the type of keys which the configuration source supports
    * @param sourceValue the value for the key from the configuration source
    * @tparam F the context in which the value exists
    * @tparam K the type of the key
    * @tparam S the type of the source value
    * @return a new [[ConfigEntry]]
    * @example {{{
    * scala> ConfigEntry.applyF[cats.Id, String, String]("key", ConfigKeyType.Environment, Right("value"))
    * res0: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    *
    * scala> ConfigEntry("key", ConfigKeyType.Environment, Right("value"))
    * res1: ConfigEntry[cats.Id, String, String, String] = ConfigEntry(key, Environment)
    * }}}
    */
  def applyF[F[_]: Apply, K, S](
    key: K,
    keyType: ConfigKeyType[K],
    sourceValue: F[Either[ConfigError, S]]
  ): ConfigEntry[F, K, S, S] = {
    new ConfigEntry[F, K, S, S](key, keyType, sourceValue, sourceValue)
  }

  implicit def configEntryShow[F[_], K, S, V](
    implicit showKey: Show[K],
    showKeyType: Show[ConfigKeyType[K]]
  ): Show[ConfigEntry[F, K, S, V]] = Show.show { entry =>
    val key = showKey.show(entry.key)
    val keyType = showKeyType.show(entry.keyType)
    s"ConfigEntry($key, $keyType)"
  }
}
