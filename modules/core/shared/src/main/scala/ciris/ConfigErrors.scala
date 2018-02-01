package ciris

/**
  * [[ConfigErrors]] represents one or more [[ConfigError]] errors that occurred
  * while reading one or more [[ConfigEntry]] configuration entries. To create a
  * [[ConfigErrors]] instance, use the [[ConfigErrors#apply]] method.
  *
  * {{{
  * scala> val errors = ConfigErrors(ConfigError("error1"), ConfigError("error2"))
  * errors: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
  * }}}
  *
  * It is possible to retrieve the underlying [[ConfigError]] errors in a `Vector`
  * with [[toVector]]. This will not incur any additional overhead, as the class
  * is a value class directly wrapping the `Vector`.
  *
  * {{{
  * scala> errors.toVector
  * res0: Vector[ConfigError] = Vector(ConfigError(error1), ConfigError(error2))
  * }}}
  *
  * There are also methods for retrieving the [[messages]] and [[size]].
  *
  * {{{
  * scala> errors.messages
  * res1: Vector[String] = Vector(error1, error2)
  *
  * scala> errors.size
  * res2: Int = 2
  * }}}
  *
  * It is also possible to append more [[ConfigError]] errors using the [[append]]
  * method, returning a new [[ConfigErrors]] instance.
  *
  * {{{
  * scala> val errors2 = errors append ConfigError("error3")
  * errors2: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2), ConfigError(error3))
  *
  * scala> errors2.messages
  * res3: Vector[String] = Vector(error1, error2, error3)
  *
  * scala> errors2.size
  * res4: Int = 3
  * }}}
  *
  * @param toVector a non-empty `Vector` of [[ConfigError]] errors
  */
final class ConfigErrors private (val toVector: Vector[ConfigError]) extends AnyVal {

  /**
    * Appends a [[ConfigError]] error to this [[ConfigErrors]], leaving this
    * [[ConfigErrors]] unmodified. Errors will be appended in order, so that
    * the specified error will be last when using methods like [[toVector]].
    *
    * @param error the [[ConfigError]] error to append
    * @return a new [[ConfigErrors]] with the error appended
    * @example {{{
    * scala> val errors = ConfigErrors(ConfigError("error1")) append ConfigError("error2")
    * errors: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
    *
    * scala> errors.toVector
    * res0: Vector[ConfigError] = Vector(ConfigError(error1), ConfigError(error2))
    * }}}
    * @example you can use [[ConfigError#append]] as a convenience method to create
    *          a new [[ConfigErrors]] instance if you have two [[ConfigError]]s.
    * {{{
    * scala> ConfigError("error1") append ConfigError("error2")
    * res1: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  def append(error: ConfigError): ConfigErrors =
    new ConfigErrors(toVector :+ error)

  /**
    * Returns the error messages of the errors in this [[ConfigErrors]].
    * The messages are guaranteed to be in the same order as the errors.
    *
    * @return the error messages of the contained errors
    * @example {{{
    * scala> ConfigErrors(ConfigError("error1"), ConfigError("error2")).messages
    * res0: Vector[String] = Vector(error1, error2)
    * }}}
    */
  def messages: Vector[String] =
    toVector.map(_.message)

  /**
    * Returns the number of errors in this [[ConfigErrors]].
    *
    * @return the number of contained errors
    * @example {{{
    * scala> ConfigErrors(ConfigError("error1")).size
    * res0: Int = 1
    * }}}
    */
  def size: Int =
    toVector.size

  /**
    * Converts this [[ConfigErrors]] to a [[ConfigException]]
    * which can be thrown.
    *
    * @return a new [[ConfigException]]
    */
  def toException: ConfigException =
    ConfigException(this)

  override def toString: String =
    s"ConfigErrors(${toVector.mkString(", ")})"
}

object ConfigErrors {

  /**
    * Creates a new [[ConfigErrors]] instance using the specified errors.
    *
    * @param first the first error
    * @param rest any remaining errors
    * @return a new [[ConfigErrors]] instance
    * @example {{{
    * scala> ConfigErrors(ConfigError("error1"), ConfigError("error2"))
    * res0: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  def apply(first: ConfigError, rest: ConfigError*): ConfigErrors =
    new ConfigErrors(first +: rest.toVector)
}
