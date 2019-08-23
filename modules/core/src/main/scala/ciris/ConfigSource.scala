package ciris

import ciris.api._
import ciris.api.syntax._
import ciris.ConfigError.{left, missingKey, readException, right}

import java.io.{File => JFile}
import java.nio.charset.Charset

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * [[ConfigSource]] represents the ability to read values of type `V` for
  * keys of type `K` from some source: for example, environment variables,
  * system properties, command-line arguments, or some vault service. The
  * values are returned in a context `F`, which can be [[api.Id]] when no
  * context is desired.<br>
  * <br>
  * You can create a [[ConfigSource]] by directly extending the class and
  * implementing [[ConfigSource#read]], or by using any of the helpers in
  * the companion object, like [[ConfigSource#apply]].<br>
  * <br>
  * The [[ConfigSource]]s already defined in the Ciris core module
  * include the following:<br>
  * - [[ConfigSource#Environment]]: for reading environment variables,<br>
  * - [[ConfigSource#Properties]]: for reading system properties, and<br>
  * - `ConfigSource.File`: for reading file contents.
  *
  * @param keyType the name and type of keys the source supports
  * @tparam K the type of keys the source supports
  * @tparam V the type of values the source reads
  * @example {{{
  * scala> val source = ConfigSource(ConfigKeyType[String]("identity key"))(key => Right(key))
  * source: ConfigSource[api.Id, String, String] = ConfigSource(ConfigKeyType(identity key))
  *
  * scala> source.read("key").toStringWithValues
  * res0: String = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
  * }}}
  */
abstract class ConfigSource[F[_], K, V](val keyType: ConfigKeyType[K]) { self =>

  /**
    * Reads the value of type `V` for the specified key of type `K`,
    * and returns a [[ConfigEntry]] with the result wrapped in the
    * context `F`. If there was an error while reading the value,
    * [[ConfigEntry#value]] will have more details.
    *
    * @param key the key for which to read the value
    * @return a [[ConfigEntry]] with the result
    * @example {{{
    * scala> ConfigSource.Environment.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def read(key: K): ConfigEntry[F, K, V, V]

  /**
    * Suspends the reading of this configuration source into context `G`.
    *
    * @param f the natural transformation from `F` to `G`
    * @tparam G the context in which to suspend the reading
    * @return a new [[ConfigSource]]
    */
  final def suspendF[G[_]: Sync](implicit f: F ~> G): ConfigSource[G, K, V] =
    ConfigSource.applyF(keyType) { key =>
      Sync[G].suspend(f(self.read(key).value))
    }

  /**
    * Transforms the context `F`, for the source, to context `G`.
    *
    * @param f the natural transformation from `F` to `G`
    * @tparam G the context to which `F` should be transformed
    * @return a new [[ConfigSource]]
    */
  final def transformF[G[_]: Apply](implicit f: F ~> G): ConfigSource[G, K, V] =
    ConfigSource.applyF(keyType) { key =>
      self.read(key).transformF[G].value
    }
}

object ConfigSource {

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning either a [[ConfigError]] or a value.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning an error or value for a key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource(ConfigKeyType[String]("identity key"))(key => Right(key))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(ConfigKeyType(identity key))
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
    * }}}
    */
  def apply[K, V](keyType: ConfigKeyType[K])(
    read: K => Either[ConfigError, V]
  ): ConfigSource[Id, K, V] = {
    ConfigSource.applyF[Id, K, V](keyType)(read)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning either a [[ConfigError]] or a value, wrapped in a context
    * of type `F` -- which can be [[api.Id]] if no context is desired.
    * [[ConfigSource#apply]] exists for the case where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning an error or value for a key
    * @tparam F the context in which the values exists
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.applyF[api.Id, String, String](ConfigKeyType[String]("identity key"))(key => Right(key))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(ConfigKeyType(identity key))
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
    * }}}
    */
  def applyF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    read: K => F[Either[ConfigError, V]]
  ): ConfigSource[F, K, V] = {
    val entry = (key: K) => ConfigEntry.applyF(key, keyType, read(key))
    new ConfigSource[F, K, V](keyType) {
      override def read(key: K): ConfigEntry[F, K, V, V] = entry(key)
      override def toString: String = s"ConfigSource($keyType)"
    }
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning an optional value.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning an optional value for a key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromOption(ConfigKeyType.Environment)(sys.env.get)
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromOption[K, V](keyType: ConfigKeyType[K])(
    read: K => Option[V]
  ): ConfigSource[Id, K, V] = {
    ConfigSource.fromOptionF[Id, K, V](keyType)(read)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning an optional value, wrapped in a context of type `F` --
    * which can be [[api.Id]] if no context is desired. There exists
    * a [[ConfigSource#fromOption]] for the case where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning an optional value for a key
    * @tparam F the context in which the values exists
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromOptionF[api.Id, String, String](ConfigKeyType.Environment)(sys.env.get)
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromOptionF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    read: K => F[Option[V]]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType) { key =>
      read(key).map {
        case Some(value) => Right(value)
        case None        => Left(missingKey(key, keyType))
      }
    }
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source is always empty -- that is, it has no value for any
    * key of type `K`.
    *
    * @param keyType the name and type of keys the source supports
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def empty[K, V](keyType: ConfigKeyType[K]): ConfigSource[Id, K, V] =
    ConfigSource.emptyF(keyType)

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source is always empty -- that is, it has no value for any
    * key of type `K`. The errors for the missing keys will be wrapped in
    * a context of type `F` -- which can be [[api.Id]] if no context is
    * desired. There exists a [[ConfigSource#empty]] for the case
    * where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @tparam F the context in which the errors are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def emptyF[F[_]: Applicative, K, V](keyType: ConfigKeyType[K]): ConfigSource[F, K, V] =
    ConfigSource.fromOptionF(keyType)(_ => Option.empty[V].pure[F])

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source always returns the specified value -- that is, for
    * every key of type `K`, the specified value is returned.
    *
    * @param keyType the name and type of keys the source supports
    * @param value the value to always return for every key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def always[K, V](keyType: ConfigKeyType[K])(
    value: V
  ): ConfigSource[Id, K, V] = {
    ConfigSource.alwaysF[Id, K, V](keyType)(value)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source always returns the specified value -- that is, for
    * every key of type `K`, the specified value is returned. The value
    * is wrapped in a context of type `F` -- which can be [[api.Id]] if
    * no context is desired. There exists a [[ConfigSource#always]]
    * for the case where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param value the value to always return for every key
    * @tparam F the context in which the value is wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def alwaysF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    value: F[V]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType)(_ => value.map(Right.apply))
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source always return the specified [[ConfigError]] -- that
    * is, for every key of type `K`, the specified error is returned.
    *
    * @param keyType the name and type of keys the source supports
    * @param error the error to always return for every key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.failed[String, String](ConfigKeyType.Environment)(ConfigError("error"))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key1").toStringWithValues
    * res0: String = ConfigEntry(key1, Environment, Left(ConfigError(error)))
    *
    * scala> source.read("key2").toStringWithValues
    * res1: String = ConfigEntry(key2, Environment, Left(ConfigError(error)))
    * }}}
    */
  def failed[K, V](keyType: ConfigKeyType[K])(
    error: ConfigError
  ): ConfigSource[Id, K, V] = {
    ConfigSource.failedF[Id, K, V](keyType)(error)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source always return the specified [[ConfigError]] -- that
    * is, for every key of type `K`, the specified error is returned. The
    * error is wrapped in the context `F` -- which can be [[api.Id]] if
    * no context is desired. [[ConfigSource#failed]] exists for the
    * case when `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param error the error to always return for every key
    * @tparam F the context in which the error is wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.failedF[api.Id, String, String](ConfigKeyType.Environment)(ConfigError("error"))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key1").toStringWithValues
    * res0: String = ConfigEntry(key1, Environment, Left(ConfigError(error)))
    *
    * scala> source.read("key2").toStringWithValues
    * res1: String = ConfigEntry(key2, Environment, Left(ConfigError(error)))
    * }}}
    */
  def failedF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    error: F[ConfigError]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType)(_ => error.map(Left.apply))
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and `Map` with keys of type `K` and values of type `V`. For every
    * key `K` in the `Map`, the corresponding value `V` is returned.
    *
    * @param keyType the name and type of keys the source supports
    * @param map the map which entries the source should contain
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromMap(ConfigKeyType.Environment)(sys.env)
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromMap[K, V](keyType: ConfigKeyType[K])(
    map: Map[K, V]
  ): ConfigSource[Id, K, V] = {
    ConfigSource.fromMapF[Id, K, V](keyType)(map)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and `Map` with keys of type `K` and values of type `V`. For every
    * key `K` in the `Map`, the corresponding value `V` is returned,
    * wrapped in the context `F` -- which can be [[api.Id]] if no
    * context is desired. [[ConfigSource#fromMap]] also exists
    * for the case when `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param map the map which entries the source should contain
    * @tparam F the context in which the map values are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromMapF[api.Id, String, String](ConfigKeyType.Environment)(sys.env)
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromMapF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    map: F[Map[K, V]]
  ): ConfigSource[F, K, V] = {
    ConfigSource.fromOptionF(keyType)(key => map.map(_.get(key)))
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and entries with keys of type `K` and values of type `V`. For every
    * key `K` in the entries, the corresponding value `V` is returned. If
    * there is more than one entry with the same key, the value in the
    * last entry is the one returned.
    *
    * @param keyType the name and type of keys the source supports
    * @param entries the entries which the source should contain
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromEntries(ConfigKeyType.Argument)(0 -> "abc", 0 -> "def", 1 -> "ghi")
    * source: ConfigSource[api.Id, Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(def))
    *
    * scala> source.read(1).toStringWithValues
    * res1: String = ConfigEntry(1, Argument, Right(ghi))
    *
    * scala> source.read(2).toStringWithValues
    * res2: String = ConfigEntry(2, Argument, Left(MissingKey(2, Argument)))
    * }}}
    */
  def fromEntries[K, V](keyType: ConfigKeyType[K])(
    entries: (K, V)*
  ): ConfigSource[Id, K, V] = {
    ConfigSource.fromEntriesF[Id, K, V](keyType)(entries)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and entries with keys of type `K` and values of type `V`. For every
    * key `K` in the entries, the corresponding value `V` is returned. If
    * there is more than one entry with the same key, the value in the
    * last entry is the one returned.<br>
    * <br>
    * The entries are wrapped in a context `F` -- which can be [[api.Id]]
    * if no context is desired. [[ConfigSource#fromEntries]] also exists
    * for the case where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param entries the entries which the source should contain
    * @tparam F the context in which the values are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def fromEntriesF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    entries: F[Seq[(K, V)]]
  ): ConfigSource[F, K, V] = {
    ConfigSource.fromMapF(keyType)(entries.map(_.toMap))
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning a `Try[V]`. The source will only contain entries where
    * the specified function returns `Success[V]` for the key `K`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning `Try[V]` for a key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromTry(ConfigKeyType.Argument)(index => scala.util.Try(Vector("a")(index)))
    * source: ConfigSource[api.Id, Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(a))
    * }}}
    */
  def fromTry[K, V](keyType: ConfigKeyType[K])(
    read: K => Try[V]
  ): ConfigSource[Id, K, V] = {
    ConfigSource.fromTryF[Id, K, V](keyType)(read)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning a `F[Try[V]]`. The source will only contain entries where
    * the specified function returns `Success[V]` for the key `K`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning `Try[V]` for a key
    * @tparam F the context in which values are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def fromTryF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    read: K => F[Try[V]]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType) { key =>
      read(key).map {
        case Success(value) => Right(value)
        case Failure(cause) => Left(readException(key, keyType, cause))
      }
    }
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning a `Try[Option[V]]`. The source will only contain entries
    * where the specified function returns `Success[Some[V]]` for the
    * key `K`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning `Try[Option[V]]` for a key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromTryOption(ConfigKeyType.Property)(key => scala.util.Try(sys.props.get(key)))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Property)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Property, Left(MissingKey(key, Property)))
    *
    * scala> source.read("").toStringWithValues
    * res1: String = ConfigEntry(, Property, Left(ReadException(, Property, java.lang.IllegalArgumentException: key can't be empty)))
    * }}}
    */
  def fromTryOption[K, V](keyType: ConfigKeyType[K])(
    read: K => Try[Option[V]]
  ): ConfigSource[Id, K, V] = {
    ConfigSource.fromTryOptionF[Id, K, V](keyType)(read)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * returning a `F[Try[Option[V]]]`. The source will only contain entries
    * where the specified function returns `Success[Some[V]]`for the key `K`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning `Try[Option[V]]` for a key
    * @tparam F the context in which values are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.fromTryOptionF[api.Id, String, String](ConfigKeyType.Property)(key => scala.util.Try(sys.props.get(key)))
    * source: ConfigSource[api.Id, String, String] = ConfigSource(Property)
    *
    * scala> source.read("key").toStringWithValues
    * res0: String = ConfigEntry(key, Property, Left(MissingKey(key, Property)))
    *
    * scala> source.read("").toStringWithValues
    * res1: String = ConfigEntry(, Property, Left(ReadException(, Property, java.lang.IllegalArgumentException: key can't be empty)))
    * }}}
    */
  def fromTryOptionF[F[_]: Apply, K, V](keyType: ConfigKeyType[K])(
    read: K => F[Try[Option[V]]]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType) { key =>
      read(key).map {
        case Success(Some(value)) => Right(value)
        case Success(None)        => Left(missingKey(key, keyType))
        case Failure(cause)       => Left(readException(key, keyType, cause))
      }
    }
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * catching any non-fatal exceptions. The source will only contain
    * entries for keys `K` where the specified function does not
    * throw an exception.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning a value for a key
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.catchNonFatal(ConfigKeyType.Argument)(Vector("a"))
    * source: ConfigSource[api.Id, Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(a))
    * }}}
    */
  def catchNonFatal[K, V](keyType: ConfigKeyType[K])(
    read: K => V
  ): ConfigSource[Id, K, V] = {
    ConfigSource.catchNonFatalF[Id, K, V](keyType)(read)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading values of type `V` for keys of type `K`,
    * catching any non-fatal exceptions. The source will only contain
    * entries for keys `K` where the specified function does not
    * throw an exception.<br>
    * <br>
    * Values will be wrapped in the context `F` -- which can be [[api.Id]]
    * if no context is desired. [[ConfigSource#catchNonFatal]] exists for
    * the case where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param read a function returning a value for a key
    * @tparam F the context in which values are wrapped
    * @tparam K the type of keys the source supports
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    */
  def catchNonFatalF[F[_]: Applicative, K, V](keyType: ConfigKeyType[K])(
    read: K => F[V]
  ): ConfigSource[F, K, V] = {
    ConfigSource.applyF(keyType) { key =>
      Try(read(key)) match {
        case Success(value) => value.map(right)
        case Failure(cause) => left[V](readException(key, keyType, cause)).pure[F]
      }
    }
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and `IndexedSeq[V]`. The source will only contain entries for keys
    * where the index exist in the specified sequence.
    *
    * @param keyType the name and type of keys the source supports
    * @param indexedSeq the sequence of indexed values
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("a"))
    * source: ConfigSource[api.Id, Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1).toStringWithValues
    * res1: String = ConfigEntry(1, Argument, Left(MissingKey(1, Argument)))
    * }}}
    */
  def byIndex[V](keyType: ConfigKeyType[Int])(
    indexedSeq: IndexedSeq[V]
  ): ConfigSource[Id, Int, V] = {
    ConfigSource.byIndexF[Id, V](keyType)(indexedSeq)
  }

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and `IndexedSeq[V]`. The source will only contain entries for keys
    * where the index exist in the specified sequence. The values are
    * wrapped in context `F` -- which can be [[api.Id]] if no context
    * is desired. [[ConfigSource#byIndex]] also exists for the case
    * where `F` is `Id`.
    *
    * @param keyType the name and type of keys the source supports
    * @param indexedSeq the sequence of indexed values
    * @tparam F the context in which values are wrapped
    * @tparam V the type of values the source reads
    * @return a new [[ConfigSource]]
    * @example {{{
    * scala> val source = ConfigSource.byIndexF[api.Id, String](ConfigKeyType.Argument)(Vector("a"))
    * source: ConfigSource[api.Id, Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0).toStringWithValues
    * res0: String = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1).toStringWithValues
    * res1: String = ConfigEntry(1, Argument, Left(MissingKey(1, Argument)))
    * }}}
    */
  def byIndexF[F[_]: Apply, V](keyType: ConfigKeyType[Int])(
    indexedSeq: F[IndexedSeq[V]]
  ): ConfigSource[F, Int, V] = {
    ConfigSource.fromOptionF(keyType) { index =>
      indexedSeq.map {
        case seq if 0 <= index && index < seq.length => Some(seq(index))
        case _                                       => Option.empty[V]
      }
    }
  }

  /**
    * [[ConfigSource]] reading environment variables from `String` keys.
    */
  object Environment extends ConfigSource[Id, String, String](ConfigKeyType.Environment) {
    private val delegate: ConfigSource[Id, String, String] =
      ConfigSource.fromMap(keyType)(sys.env)

    override def read(key: String): ConfigEntry[api.Id, String, String, String] =
      delegate.read(key)

    override def toString: String =
      "Environment"
  }

  /**
    * [[ConfigSource]] reading system properties from `String` keys.
    */
  object Properties extends ConfigSource[Id, String, String](ConfigKeyType.Property) {
    private val delegate: ConfigSource[Id, String, String] =
      ConfigSource.fromTryOption(keyType)(key => Try(sys.props.get(key)))

    override def read(key: String): ConfigEntry[api.Id, String, String, String] =
      delegate.read(key)

    override def toString: String =
      "Properties"
  }

  /**
    * [[ConfigSource]] reading file contents from `File` and `Charset` keys.
    */
  object File extends ConfigSource[Id, (JFile, Charset), String](ConfigKeyType.File) {
    private val delegate: ConfigSource[Id, (JFile, Charset), String] =
      ConfigSource.catchNonFatal(keyType) {
        case (file, charset) =>
          Source.fromFile(file, charset.name).mkString
      }

    override def read(key: (JFile, Charset)): ConfigEntry[Id, (JFile, Charset), String, String] =
      delegate.read(key)

    override def toString: String =
      "File"
  }
}
