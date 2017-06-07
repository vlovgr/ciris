package ciris

/**
  * A class representing one or more errors that occurred while reading
  * a single [[ConfigValue]] configuration value. An error is basically
  * a String message, and can be created from one by using the
  * [[ConfigError#apply]] method in the companion object.
  *
  * {{{
  * scala> val error = ConfigError("error")
  * error: ConfigError = ConfigError(error)
  * }}}
  *
  * The error message can be retrieved again using [[message]].<br>
  * <br>
  * [[ConfigError]]s can be combined into a single [[ConfigError]] using
  * the [[combine]] method. This is useful when there was more than one
  * error when reading the configuration value.
  *
  * {{{
  * scala> error combine ConfigError("error2")
  * res0: ConfigError = Combined(Vector(ConfigError(error), ConfigError(error2)))
  * }}}
  *
  * There is also a convenience method [[append]] for creating [[ConfigErrors]].
  *
  * {{{
  * scala> error append ConfigError("error2")
  * res1: ConfigErrors = ConfigErrors(ConfigError(error), ConfigError(error2))
  * }}}
  *
  * The companion object contains methods for creating the most common
  * [[ConfigError]]s, like:<br>
  * <br>
  * - [[ConfigError#missingKey]] for when there is no value for a specified key,<br>
  * - [[ConfigError#readException]] for when an error occurred while reading a key, and<br>
  * - [[ConfigError#wrongType]] for when the value couldn't be converted to the expected type.
  */
sealed abstract class ConfigError {

  /**
    * The String error message for this [[ConfigError]].
    *
    * @return the error message
    * @example {{{
    * scala> ConfigError("error").message
    * res0: String = error
    * }}}
    */
  def message: String

  /**
    * Combines this [[ConfigError]] with that [[ConfigError]] to create
    * a new [[ConfigError]] with both errors' messages. This is useful
    * for when there is more than one error when reading a value.
    *
    * [[ConfigError]]s are combined in order so that the message of this
    * [[ConfigError]] is before the message of that [[ConfigError]].
    *
    * @param that the [[ConfigError]] to combine with this [[ConfigError]]
    * @return a new [[ConfigError]] with both errors' messages
    * @example {{{
    * scala> ConfigError("error1") combine ConfigError("error2")
    * res0: ConfigError = Combined(Vector(ConfigError(error1), ConfigError(error2)))
    * }}}
    */
  final def combine(that: ConfigError): ConfigError =
    ConfigError.combined(this, that)

  /**
    * Appends that [[ConfigError]] to this [[ConfigError]] to create a
    * new [[ConfigErrors]] instance. This is useful for when reading
    * more than one value and errors need to be accumulated.
    *
    * [[ConfigError]]s are appended in order so that this [[ConfigError]]
    * is before that [[ConfigError]] in the resulting [[ConfigErrors]].
    *
    * @param that the [[ConfigError]] to append
    * @return a new [[ConfigErrors]] instance containing this
    *         [[ConfigError]] followed by that [[ConfigError]]
    * @example {{{
    * scala> ConfigError("error1") append ConfigError("error2")
    * res0: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  final def append(that: ConfigError): ConfigErrors =
    ConfigErrors(this, that)
}

object ConfigError {

  /**
    * Creates a new [[ConfigError]] with the specified message.
    *
    * @param message the message to use for the [[ConfigError]]
    * @return a new [[ConfigError]] with the specified message
    * @note note that the message is passed by reference, meaning it will not
    *       be evaluated until the [[ConfigError#message]] method is invoked.
    * @example {{{
    * scala> ConfigError("error1")
    * res0: ConfigError = ConfigError(error1)
    * }}}
    */
  def apply(message: => String): ConfigError = {
    def theMessage = message
    new ConfigError {
      override def message: String = theMessage
      override def toString: String = s"ConfigError($message)"
    }
  }

  /**
    * A [[ConfigError]] which is the combination of at least two other [[ConfigError]]s.
    * This is useful when there is more than one error when reading a value. To create
    * a [[Combined]] error using other [[ConfigError]]s, use the [[combined]] method.
    *
    * {{{
    * scala> ConfigError.combined(ConfigError("error1"), ConfigError("error2"))
    * res0: ConfigError = Combined(Vector(ConfigError(error1), ConfigError(error2)))
    * }}}
    *
    * Alternatively, use the convenience method on [[ConfigError]].
    *
    * {{{
    * scala> ConfigError("error1") combine ConfigError("error2")
    * res1: ConfigError = Combined(Vector(ConfigError(error1), ConfigError(error2)))
    * }}}
    *
    * @param errors two or more errors to combine
    */
  private sealed class Combined(val errors: Vector[ConfigError]) extends ConfigError {
    override def message: String = errors.map(_.message).mkString(", ")
    override def toString: String = s"Combined($errors)"
  }

  /**
    * Combines two or more [[ConfigError]]s into a single [[ConfigError]].
    * This is useful when there is more than one error when reading a value.
    *
    * @param first the first [[ConfigError]] to combine
    * @param second the second [[ConfigError]] to combine
    * @param rest any remaining [[ConfigError]]s to combine
    * @return a new [[ConfigError]] combining all specified [[ConfigError]]s
    * @example you can use the [[ConfigError#combined]] method.
    * {{{
    * scala> ConfigError.combined(ConfigError("error1"), ConfigError("error2"))
    * res0: ConfigError = Combined(Vector(ConfigError(error1), ConfigError(error2)))
    * }}}
    * @example you can use the [[ConfigError#combine]] method.
    * {{{
    * scala> ConfigError("error1") combine ConfigError("error2")
    * res1: ConfigError = Combined(Vector(ConfigError(error1), ConfigError(error2)))
    * }}}
    */
  def combined(first: ConfigError, second: ConfigError, rest: ConfigError*): ConfigError =
    new Combined(Vector(first, second) ++ rest)

  /**
    * A [[ConfigError]] representing that a key is missing from the configuration source,
    * that is, that there is no value for a specified key. Accepts a key value of type
    * Key and a matching [[ConfigKeyType]].
    *
    * To create a new [[MissingKey]] error, use the [[missingKey]] method.
    *
    * {{{
    * scala> val error = ConfigError.missingKey("key", ConfigKeyType.Environment)
    * error: ConfigError = MissingKey(key, Environment)
    *
    * scala> error.message
    * res0: String = Missing environment variable [key]
    * }}}
    *
    * @param key the key which is missing from the configuration source
    * @param keyType the type of keys the configuration source reads
    * @tparam Key the type of the key
    */
  private sealed class MissingKey[Key](key: Key, keyType: ConfigKeyType[Key]) extends ConfigError {
    override def message: String = s"Missing ${keyType.name} [$key]"
    override def toString: String = s"MissingKey($key, $keyType)"
  }

  /**
    * Creates a new error representing the fact that a key is missing from the
    * configuration source, that is, that there is no value for a specified key.
    * Accepts a key value of type `Key` and a matching [[ConfigKeyType]].
    *
    * @param key the key which is missing from the configuration source
    * @param keyType the type of keys the configuration source reads
    * @tparam Key the type of the key
    * @return a new error using the specified key and key type
    * @example {{{
    * scala> val error = ConfigError.missingKey("key", ConfigKeyType.Environment)
    * error: ConfigError = MissingKey(key, Environment)
    *
    * scala> error.message
    * res0: String = Missing environment variable [key]
    * }}}
    */
  def missingKey[Key](key: Key, keyType: ConfigKeyType[Key]): ConfigError =
    new MissingKey[Key](key, keyType)

  /**
    * A [[ConfigError]] representing that there was an exception while reading a key
    * from a configuration source. Accepts a key value of type `Key`, a matching
    * [[ConfigKeyType]], and the `Throwable` cause.
    *
    * To create a new [[ReadException]] error, use the [[readException]] method.
    *
    * {{{
    * scala> val error = ConfigError.readException("key", ConfigKeyType.Environment, new Error("error"))
    * error: ConfigError = ReadException(key, Environment, java.lang.Error: error)
    *
    * scala> error.message
    * res0: String = Exception while reading environment variable [key]: java.lang.Error: error
    * }}}
    *
    * @param key the key for which there was a read exception
    * @param keyType the type of keys the configuration source reads
    * @param cause the reason why there was an exception while reading
    * @tparam Key the type of the key
    */
  private sealed class ReadException[Key](key: Key, keyType: ConfigKeyType[Key], cause: Throwable) extends ConfigError {
    override def message: String = s"Exception while reading ${keyType.name} [$key]: $cause"
    override def toString: String = s"ReadException($key, $keyType, $cause)"
  }

  /**
    * Creates a new error representing the fact that there was an exception while
    * reading a key from a configuration source. Accepts a key value of type
    * `Key`, a matching [[ConfigKeyType]], and the `Throwable` cause.
    *
    * @param key the key for which there was a read exception
    * @param keyType the type of keys the configuration source reads
    * @param cause the reason why there was an exception while reading
    * @tparam Key the type of the key
    * @return a new error using the specified arguments
    * @example {{{
    * scala> val error = ConfigError.readException("key", ConfigKeyType.Environment, new Error("error"))
    * error: ConfigError = ReadException(key, Environment, java.lang.Error: error)
    *
    * scala> error.message
    * res0: String = Exception while reading environment variable [key]: java.lang.Error: error
    * }}}
    */
  def readException[Key](key: Key, keyType: ConfigKeyType[Key], cause: Throwable): ConfigError =
    new ReadException[Key](key, keyType, cause)

  /**
    * A [[ConfigError]] representing that there was an error while trying to convert a
    * configuration value to a type with name `typeName`. Accepts a key of type `Key`,
    * a matching [[ConfigKeyType]], a value of type `Value`, the name of the type for
    * which conversion was attempted, and an optional cause of type `Cause`.
    *
    * To create a new [[WrongType]] error, use the [[wrongType]] method.
    *
    * {{{
    * scala> val error = ConfigError.wrongType("key", 1.5, "Int", ConfigKeyType.Environment)
    * error: ConfigError = WrongType(key, 1.5, Int, Environment, None)
    *
    * scala> error.message
    * res0: String = Environment variable [key] with value [1.5] cannot be converted to type [Int]
    * }}}
    *
    * @param key the key for which the value was of the wrong type
    * @param value the value which could not be converted to the expected type
    * @param typeName the name of the type for which conversion was attempted
    * @param keyType the type of keys the configuration source reads
    * @param cause optionally, the reason why the conversion failed
    * @tparam Key the type of the key
    * @tparam Value the type of the value
    * @tparam Cause the type of the cause
    */
  private sealed class WrongType[Key, Value, Cause](
    key: Key,
    value: Value,
    typeName: String,
    keyType: ConfigKeyType[Key],
    cause: Option[Cause] = None
  ) extends ConfigError {
    override def message: String = {
      val causeMessage = cause.map(cause => s": $cause").getOrElse("")
      s"${keyType.name.capitalize} [$key] with value [$value] cannot be converted to type [$typeName]$causeMessage"
    }

    override def toString: String =
      s"WrongType($key, $value, $typeName, $keyType, $cause)"
  }

  /**
    * Creates a new error representing the fact that there was an error while trying to
    * convert a configuration value to a type with name `typeName`. Accepts a key of type
    * `Key`, a matching [[ConfigKeyType]], a value of type `Value`, the name of the type
    * for which conversion was attempted, and an optional cause of type `Cause`.
    *
    * @param key the key for which the value was of the wrong type
    * @param value the value which could not be converted to the expected type
    * @param typeName the name of the type for which conversion was attempted
    * @param keyType the type of keys the configuration source reads
    * @param cause optionally, the reason why the conversion failed
    * @tparam Key the type of the key
    * @tparam Value the type of the value
    * @tparam Cause the type of the cause
    * @return a new error using the specified arguments
    * @example {{{
    * scala> val error = ConfigError.wrongType("key", 1.5, "Int", ConfigKeyType.Environment)
    * error: ConfigError = WrongType(key, 1.5, Int, Environment, None)
    *
    * scala> error.message
    * res0: String = Environment variable [key] with value [1.5] cannot be converted to type [Int]
    * }}}
    */
  def wrongType[Key, Value, Cause](
    key: Key,
    value: Value,
    typeName: String,
    keyType: ConfigKeyType[Key],
    cause: Option[Cause] = None
  ): ConfigError = {
    new WrongType[Key, Value, Cause](key, value, typeName, keyType, cause)
  }
}
