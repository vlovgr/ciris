package ciris

/**
  * [[ConfigError]] represents one or more errors that occurred while reading
  * or decoding a single [[ConfigEntry]] configuration entry value. An error
  * is basically a `String` message, and can be created from one by using
  * [[ConfigError#apply]] in the companion object.
  *
  * {{{
  * scala> val error = ConfigError("error")
  * error: ConfigError = ConfigError(error)
  *
  * scala> error.message
  * res0: String = error
  * }}}
  *
  * [[ConfigError]]s can be combined into a single [[ConfigError]] using the
  * [[combine]] method. This is useful when there was more than one error
  * when reading or decoding the configuration value.
  *
  * {{{
  * scala> error combine ConfigError("error2")
  * res1: ConfigError = Combined(ConfigError(error), ConfigError(error2))
  * }}}
  *
  * There is also a convenience method [[append]] for creating [[ConfigErrors]].
  *
  * {{{
  * scala> error append ConfigError("error2")
  * res2: ConfigErrors = ConfigErrors(ConfigError(error), ConfigError(error2))
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
    * The `String` error message for this [[ConfigError]].
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
    * a new [[ConfigError]] with both error messages. This is useful
    * when there is more than one error when reading or decoding a
    * single value.<br>
    * <br>
    * [[ConfigError]]s are combined in order, so that the message of this
    * [[ConfigError]] is before the message of that [[ConfigError]].
    *
    * @param that the [[ConfigError]] to combine with this [[ConfigError]]
    * @return a new [[ConfigError]] with both errors' messages
    * @example {{{
    * scala> ConfigError("error1") combine ConfigError("error2")
    * res0: ConfigError = Combined(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  final def combine(that: ConfigError): ConfigError =
    ConfigError.combined(this, that)

  /**
    * Appends that [[ConfigError]] to this [[ConfigError]] to create a
    * new [[ConfigErrors]] instance. This is useful for when reading or
    * decoding more than one value and errors need to be accumulated.<br>
    * <br>
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

  private final class Combined(errors: Vector[ConfigError]) extends ConfigError {
    private def uncapitalize(s: String): String =
      if (s.length == 0 || s.charAt(0).isLower) s
      else {
        val chars = s.toCharArray
        chars(0) = chars(0).toLower
        new String(chars)
      }

    override def message: String = {
      val messages =
        errors
          .map(_.message)
          .filter(_.nonEmpty)
          .zipWithIndex
          .map {
            case (m, 0) => m.capitalize
            case (m, _) => uncapitalize(m)
          }

      messages match {
        case Vector()       => ""
        case Vector(m1)     => m1
        case Vector(m1, m2) => m1 ++ " and " ++ m2
        case ms             => ms.init.mkString(", ") ++ ", and " ++ ms.last
      }
    }

    override def toString: String = s"Combined(${errors.mkString(", ")})"
  }

  /**
    * Combines two or more [[ConfigError]]s into a single [[ConfigError]].
    * This is useful when there is more than one error when reading or
    * decoding a value.
    *
    * @param first the first [[ConfigError]] to combine
    * @param second the second [[ConfigError]] to combine
    * @param rest any remaining [[ConfigError]]s to combine
    * @return a new [[ConfigError]] combining all specified errors
    * @example you can use the [[ConfigError#combined]] method.
    * {{{
    * scala> ConfigError.combined(ConfigError("error1"), ConfigError("error2"))
    * res0: ConfigError = Combined(ConfigError(error1), ConfigError(error2))
    * }}}
    * @example you can use the [[ConfigError#combine]] method.
    * {{{
    * scala> ConfigError("error1") combine ConfigError("error2")
    * res1: ConfigError = Combined(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  def combined(first: ConfigError, second: ConfigError, rest: ConfigError*): ConfigError =
    new Combined(Vector(first, second) ++ rest)

  private final class MissingKey[K](key: K, keyType: ConfigKeyType[K]) extends ConfigError {
    override def message: String = s"Missing ${keyType.name} [$key]"
    override def toString: String = s"MissingKey($key, $keyType)"
  }

  /**
    * Creates a new error representing the fact that a key is missing from the
    * configuration source, that is, that there is no value for a specified key.
    * Accepts a key value of type `K` and a matching [[ConfigKeyType]].
    *
    * @param key the key which is missing from the configuration source
    * @param keyType the name and type of keys the source supports
    * @tparam K the type of the key
    * @return a new error using the specified key and key type
    * @example {{{
    * scala> val error = ConfigError.missingKey("key", ConfigKeyType.Environment)
    * error: ConfigError = MissingKey(key, Environment)
    *
    * scala> error.message
    * res0: String = Missing environment variable [key]
    * }}}
    */
  def missingKey[K](key: K, keyType: ConfigKeyType[K]): ConfigError =
    new MissingKey[K](key, keyType)

  private final class ReadException[K](
    key: K,
    keyType: ConfigKeyType[K],
    cause: Throwable
  ) extends ConfigError {
    override def message: String = s"Exception while reading ${keyType.name} [$key]: $cause"
    override def toString: String = s"ReadException($key, $keyType, $cause)"
  }

  /**
    * Creates a new error representing the fact that there was an exception while
    * reading a key from some configuration source. Accepts a key of type `K`, a
    * matching [[ConfigKeyType]], and the `Throwable` cause.
    *
    * @param key the key for which there was a read exception
    * @param keyType the name and type of keys the source supports
    * @param cause the reason why there was an exception while reading
    * @tparam K the type of the key
    * @return a new error using the specified arguments
    * @example {{{
    * scala> val error = ConfigError.readException("key", ConfigKeyType.Environment, new Error("error"))
    * error: ConfigError = ReadException(key, Environment, java.lang.Error: error)
    *
    * scala> error.message
    * res0: String = Exception while reading environment variable [key]: java.lang.Error: error
    * }}}
    */
  def readException[K](key: K, keyType: ConfigKeyType[K], cause: Throwable): ConfigError =
    new ReadException[K](key, keyType, cause)

  private final class WrongType[K, S, V, C](
    key: K,
    keyType: ConfigKeyType[K],
    sourceValue: Either[ConfigError, S],
    value: V,
    typeName: String,
    cause: Option[C]
  ) extends ConfigError {
    override def message: String = {
      val causeMessage =
        cause.map(cause => s": $cause").getOrElse("")

      val sourceValueMessage =
        sourceValue match {
          case Right(sourceValue) if sourceValue.toString != value.toString =>
            s" (and unmodified value [$sourceValue])"
          case _ =>
            ""
        }

      s"${keyType.name.capitalize} [$key] with value [$value]$sourceValueMessage cannot be converted to type [$typeName]$causeMessage"
    }

    override def toString: String = cause match {
      case Some(cause) => s"WrongType($key, $keyType, $sourceValue, $value, $typeName, $cause)"
      case None        => s"WrongType($key, $keyType, $sourceValue, $value, $typeName)"
    }
  }

  /**
    * Creates a new error representing the fact that there was an error while trying to
    * convert a configuration value to a type with name `typeName`. Accepts a key of type
    * `K`, a matching [[ConfigKeyType]], an unmodified source value of type `S`, a value
    * of type `V`, and an optional cause of type `C`.
    *
    * @param key the key for which the value was of the wrong type
    * @param value the value which could not be converted to the expected type
    * @param typeName the name of the type for which conversion was attempted
    * @param keyType the type of keys the configuration source reads
    * @param cause optionally, the reason why the conversion failed
    * @tparam K the type of the key
    * @tparam V the type of the value
    * @tparam C the type of the cause
    * @return a new error using the specified arguments
    * @example {{{
    * scala> val error = ConfigError.wrongType("key", ConfigKeyType.Environment, Right("1.5"), 1.5, "Int", None)
    * error: ConfigError = WrongType(key, Environment, Right(1.5), 1.5, Int)
    *
    * scala> error.message
    * res0: String = Environment variable [key] with value [1.5] cannot be converted to type [Int]
    * }}}
    */
  def wrongType[K, S, V, C](
    key: K,
    keyType: ConfigKeyType[K],
    sourceValue: Either[ConfigError, S],
    value: V,
    typeName: String,
    cause: Option[C]
  ): ConfigError = {
    new WrongType[K, S, V, C](key, keyType, sourceValue, value, typeName, cause)
  }

  /**
    * Wraps the specified [[ConfigError]] in an `Either[ConfigError, A]`.
    * Useful in cases where it is necessary to guide type inference and
    * widen a `Left` into an `Either[ConfigError, A]`.
    *
    * @param error the error which should be wrapped
    * @tparam A the right side value of the `Either`
    * @return the specified error as an `Either[ConfigError, A]`
    */
  def left[A](error: ConfigError): Either[ConfigError, A] =
    Left(error)

  /**
    * Wraps the specified value in an `Either[ConfigError, A]`. Useful
    * in cases where it is necessary to guide type inference and widen
    * a `Right` into an `Either[ConfigError, A]`.
    *
    * @param value the value which should be wrapped
    * @tparam A the type of the value
    * @return the specified value as an `Either[ConfigError, A]`
    */
  def right[A](value: A): Either[ConfigError, A] =
    Right(value)
}
