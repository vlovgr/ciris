package ciris

import ciris.ConfigError.{missingKey, readException}

import scala.util.{Failure, Success, Try}

/**
  * [[ConfigSource]] represents the ability to read values of type `V` for
  * keys of type `K` from some source; for example, environment variables,
  * system properties, command-line arguments, or some vault service.<br>
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
  * source: ConfigSource[String, String] = ConfigSource(ConfigKeyType(identity key))
  *
  * scala> source.read("key")
  * res0: ConfigEntry[String, String, String] = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
  * }}}
  */
abstract class ConfigSource[K, V](val keyType: ConfigKeyType[K]) {

  /**
    * Reads the value of type `V` for the specified key of type `K`,
    * and returns a [[ConfigEntry]] with the result. If there was an
    * error while reading the value, [[ConfigEntry#value]] will have
    * details on what went wrong.
    *
    * @param key the key for which to read the value
    * @return a [[ConfigEntry]] with the result
    * @example {{{
    * scala> val entry = ConfigSource.Environment.read("key")
    * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def read(key: K): ConfigEntry[K, V, V]
}

object ConfigSource extends ConfigSourcePlatformSpecific {

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
    * source: ConfigSource[String, String] = ConfigSource(ConfigKeyType(identity key))
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
    * }}}
    */
  def apply[K, V](keyType: ConfigKeyType[K])(
    read: K => Either[ConfigError, V]
  ): ConfigSource[K, V] = {
    val entry = (key: K) => ConfigEntry(key, keyType, read(key))
    new ConfigSource[K, V](keyType) {
      override def read(key: K): ConfigEntry[K, V, V] = entry(key)
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
    * source: ConfigSource[String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromOption[K, V](keyType: ConfigKeyType[K])(
    read: K => Option[V]
  ): ConfigSource[K, V] = {
    ConfigSource(keyType) { key =>
      read(key) match {
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
  def empty[K, V](keyType: ConfigKeyType[K]): ConfigSource[K, V] =
    ConfigSource.fromOption(keyType)(_ => Option.empty[V])

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
  def always[K, V](keyType: ConfigKeyType[K])(value: V): ConfigSource[K, V] =
    ConfigSource(keyType)(_ => Right(value))

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
    * source: ConfigSource[String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key1")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key1, Environment, Left(ConfigError(error)))
    *
    * scala> source.read("key2")
    * res1: ConfigEntry[String, String, String] = ConfigEntry(key2, Environment, Left(ConfigError(error)))
    * }}}
    */
  def failed[K, V](keyType: ConfigKeyType[K])(error: ConfigError): ConfigSource[K, V] =
    ConfigSource[K, V](keyType)(_ => Left(error))

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
    * source: ConfigSource[String, String] = ConfigSource(Environment)
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromMap[K, V](keyType: ConfigKeyType[K])(map: Map[K, V]): ConfigSource[K, V] =
    ConfigSource.fromOption(keyType)(map.get)

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
    * source: ConfigSource[Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0)
    * res0: ConfigEntry[Int, String, String] = ConfigEntry(0, Argument, Right(def))
    *
    * scala> source.read(1)
    * res1: ConfigEntry[Int, String, String] = ConfigEntry(1, Argument, Right(ghi))
    *
    * scala> source.read(2)
    * res2: ConfigEntry[Int, String, String] = ConfigEntry(2, Argument, Left(MissingKey(2, Argument)))
    * }}}
    */
  def fromEntries[K, V](keyType: ConfigKeyType[K])(entries: (K, V)*): ConfigSource[K, V] =
    ConfigSource.fromMap(keyType)(entries.toMap)

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
    * source: ConfigSource[Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0)
    * res0: ConfigEntry[Int, String, String] = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1)
    * res1: ConfigEntry[Int, String, String] = ConfigEntry(1, Argument, Left(ReadException(1, Argument, java.lang.IndexOutOfBoundsException: 1)))
    * }}}
    */
  def fromTry[K, V](keyType: ConfigKeyType[K])(
    read: K => Try[V]
  ): ConfigSource[K, V] = {
    ConfigSource(keyType) { key =>
      read(key) match {
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
    * source: ConfigSource[String, String] = ConfigSource(Property)
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Property, Left(MissingKey(key, Property)))
    *
    * scala> source.read("")
    * res1: ConfigEntry[String, String, String] = ConfigEntry(, Property, Left(ReadException(, Property, java.lang.IllegalArgumentException: key can't be empty)))
    * }}}
    */
  def fromTryOption[K, V](keyType: ConfigKeyType[K])(
    read: K => Try[Option[V]]
  ): ConfigSource[K, V] = {
    ConfigSource(keyType) { key =>
      read(key) match {
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
    * source: ConfigSource[Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0)
    * res0: ConfigEntry[Int, String, String] = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1)
    * res1: ConfigEntry[Int, String, String] = ConfigEntry(1, Argument, Left(ReadException(1, Argument, java.lang.IndexOutOfBoundsException: 1)))
    * }}}
    */
  def catchNonFatal[K, V](keyType: ConfigKeyType[K])(read: K => V): ConfigSource[K, V] =
    ConfigSource.fromTry(keyType)(key => Try(read(key)))

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
    * source: ConfigSource[Int, String] = ConfigSource(Argument)
    *
    * scala> source.read(0)
    * res0: ConfigEntry[Int, String, String] = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1)
    * res1: ConfigEntry[Int, String, String] = ConfigEntry(1, Argument, Left(MissingKey(1, Argument)))
    * }}}
    */
  def byIndex[V](keyType: ConfigKeyType[Int])(indexedSeq: IndexedSeq[V]): ConfigSource[Int, V] =
    ConfigSource.fromOption(keyType) { index =>
      if (0 <= index && index < indexedSeq.length)
        Some(indexedSeq(index))
      else
        None
    }

  /**
    * [[ConfigSource]] reading environment variables from `String` keys.
    */
  object Environment extends ConfigSource[String, String](ConfigKeyType.Environment) {
    private val delegate: ConfigSource[String, String] =
      ConfigSource.fromMap(keyType)(sys.env)

    override def read(key: String): ConfigEntry[String, String, String] =
      delegate.read(key)

    override def toString: String =
      "Environment"
  }

  /**
    * [[ConfigSource]] reading system properties from `String` keys.
    */
  object Properties extends ConfigSource[String, String](ConfigKeyType.Property) {
    private val delegate: ConfigSource[String, String] =
      ConfigSource.fromTryOption(keyType)(key => Try(sys.props.get(key)))

    override def read(key: String): ConfigEntry[String, String, String] =
      delegate.read(key)

    override def toString: String =
      "Properties"
  }
}
