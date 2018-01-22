package ciris

import ciris.ConfigError.{missingKey, readException}

import scala.util.{Failure, Success, Try}

/**
  * A [[ConfigSource]] represents the ability to read keys of type `K`,
  * retrieving a value of type `V`, from some source; for example from,
  * environment variables, system properties, command-line arguments,
  * or a vault service.
  *
  * You can create a [[ConfigSource]] by directly extending the class, or
  * by using the [[ConfigSource#apply]] method in the companion object.
  *
  * {{{
  * scala> val source = ConfigSource(ConfigKeyType[String]("identity key"))(key => Right(key))
  * source: ConfigSource[String] = ConfigSource(ConfigKeyType(identity key))
  *
  * scala> source.read("key")
  * res0: ConfigEntry[String, String, String] = ConfigEntry(key, ConfigKeyType(identity key), Right(key))
  * }}}
  *
  * There are also convenience methods in the companion object for creating
  * sources from some different constructs, like [[ConfigSource#fromOption]],
  * [[ConfigSource#fromMap]], [[ConfigSource#fromTry]], and more.
  *
  * The [[read]] method returns a [[ConfigEntry]], which is the key-value
  * pair that was read, along with the [[ConfigKeyType]] of the [[ConfigSource]].
  *
  * @param keyType the [[ConfigKeyType]] representing the key type and name
  * @tparam K the type of keys the source can read
  * @tparam V the type of values the source retrieves
  */
abstract class ConfigSource[K, V](val keyType: ConfigKeyType[K]) {

  /**
    * Reads the specified key of type `K` and returns the key along
    * with the value in a [[ConfigEntry]]. If there was an error
    * while reading the value, [[ConfigEntry#value]] will have
    * a [[ConfigError]] instead of the value.
    *
    * @param key the key to read from the configuration source
    * @return a [[ConfigEntry]] containing the key-value pair
    * @example {{{
    * scala> ConfigSource.Environment.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def read(key: K): ConfigEntry[K, V, V]
}

object ConfigSource extends ConfigSourcePlatformSpecific {

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]] and
    * read function, reading keys of the type `K`, and either returning a
    * [[ConfigError]] or a `String` value.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param read function reading keys, returning an error or a String value
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource(ConfigKeyType[String]("identity key"))(key => Right(key))
    * source: ConfigSource[String] = ConfigSource(ConfigKeyType(identity key))
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
    * and read function, reading keys of the type `K`, and returning
    * an optional `String` value.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param read function reading keys, returning an optional `String` value
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.fromOption(ConfigKeyType.Environment)(sys.env.get)
    * source: ConfigSource[String] = ConfigSource(Environment)
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromOption[K, V](keyType: ConfigKeyType[K])(read: K => Option[V]): ConfigSource[K, V] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Some(value) => Right(value)
        case None        => Left(missingKey(key, keyType))
    })

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the source is always empty - that is, it has no value for any
    * key of type `K`.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new empty [[ConfigSource]] without any entries
    */
  def empty[K, V](keyType: ConfigKeyType[K]): ConfigSource[K, V] =
    ConfigSource.fromOption(keyType)(_ => Option.empty[V])

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]],
    * where the specified [[ConfigError]] will be returned for every read
    * key of type `K`.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param error the error which will be returned for every read key
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new failed [[ConfigSource]] always returning the specified error
    * @example {{{
    * scala> val source = ConfigSource.failed[String, String](ConfigKeyType.Environment)(ConfigError("error"))
    * source: ConfigSource[String] = ConfigSource(Environment)
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
    * and `Map` with keys of type `K`.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param map the Map from which to read keys, keys are of type K
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.fromMap(ConfigKeyType.Environment)(sys.env)
    * source: ConfigSource[String] = ConfigSource(Environment)
    *
    * scala> source.read("key")
    * res0: ConfigEntry[String, String, String] = ConfigEntry(key, Environment, Left(MissingKey(key, Environment)))
    * }}}
    */
  def fromMap[K, V](keyType: ConfigKeyType[K])(map: Map[K, V]): ConfigSource[K, V] =
    ConfigSource.fromOption(keyType)(map.get)

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and entries with keys of type `K`. If the same key appears more
    * than once in the entries, the last one will be chosen to be
    * included in the source.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param entries the entries to be included in the source
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.fromEntries(ConfigKeyType.Argument)(0 -> "abc", 0 -> "def", 1 -> "ghi")
    * source: ConfigSource[Int] = ConfigSource(Argument)
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
    * and read function, reading keys of the type `K`, and returning
    * a value wrapped in a `Try`. The source will only successfully
    * read values for keys where the function returns `Success`.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param read function reading keys, returning a `String` value in a `Try`
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.fromTry(ConfigKeyType.Argument)(index => scala.util.Try(Vector("a")(index)))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> source.read(0)
    * res0: ConfigEntry[Int, String, String] = ConfigEntry(0, Argument, Right(a))
    *
    * scala> source.read(1)
    * res1: ConfigEntry[Int, String, String] = ConfigEntry(1, Argument, Left(ReadException(1, Argument, java.lang.IndexOutOfBoundsException: 1)))
    * }}}
    */
  def fromTry[K, V](keyType: ConfigKeyType[K])(read: K => Try[V]): ConfigSource[K, V] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Success(value) => Right(value)
        case Failure(cause) => Left(readException(key, keyType, cause))
    })

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading keys of the type `K`, and returning
    * a value wrapped in a `Try` and `Option`. The source will only
    * successfully read value for keys where the read function
    * returns both a `Success` and a `Some`.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param read function reading keys, returning a `String` value in a `Try` and `Option`
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.fromTryOption(ConfigKeyType.Property)(key => scala.util.Try(sys.props.get(key)))
    * source: ConfigSource[String] = ConfigSource(Property)
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
  ): ConfigSource[K, V] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Success(Some(value)) => Right(value)
        case Success(None)        => Left(missingKey(key, keyType))
        case Failure(cause)       => Left(readException(key, keyType, cause))
    })

  /**
    * Creates a new [[ConfigSource]] from the specified [[ConfigKeyType]]
    * and read function, reading keys of the type `K`, and returning
    * `String` values. The read function will be wrapped in a `Try` and
    * only the successful cases will result in values for the source.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param read function reading keys, returning String values
    * @tparam K the type of keys which the source supports
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.catchNonFatal(ConfigKeyType.Argument)(Vector("a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
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
    * and `IndexedSeq`, where values are read by specifying indexes in
    * the collection. Only values for which the key index exist in the
    * collection will be in the configuration source.
    *
    * @param keyType the [[ConfigKeyType]] representing the key type and name
    * @param indexedSeq the collection from which to read `String` values
    * @tparam V the type of values which the source returns
    * @return a new [[ConfigSource]] using the specified arguments
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
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
  case object Environment extends ConfigSource[String, String](ConfigKeyType.Environment) {
    private val delegate: ConfigSource[String, String] =
      ConfigSource.fromMap(keyType)(sys.env)

    override def read(key: String): ConfigEntry[String, String, String] =
      delegate.read(key)
  }

  /**
    * [[ConfigSource]] reading system properties from `String` keys.
    */
  case object Properties extends ConfigSource[String, String](ConfigKeyType.Property) {
    private val delegate: ConfigSource[String, String] =
      ConfigSource.fromTryOption(keyType)(key => Try(sys.props.get(key)))

    override def read(key: String): ConfigEntry[String, String, String] =
      delegate.read(key)
  }
}
