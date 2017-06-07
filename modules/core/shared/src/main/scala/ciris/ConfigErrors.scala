package ciris

/**
  * A value class representing one or more [[ConfigError]] errors that occurred
  * while reading one or more [[ConfigValue]] configuration values. To create a
  * [[ConfigErrors]] instance, use the [[ConfigErrors#apply]] method in the
  * companion object.
  *
  * {{{
  * scala> val errors = ConfigErrors(ConfigError("error1"), ConfigError("error2"))
  * errors: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
  * }}}
  *
  * You can retrieve the [[ConfigError]] instances in a `Vector` by using
  * [[toVector]]. This will not incur any additional overhead, as the class
  * is a value class directly wrapping the `Vector`.
  *
  * {{{
  * scala> errors.toVector
  * res0: Vector[ConfigError] = Vector(ConfigError(error1), ConfigError(error2))
  * }}}
  *
  * There are also convenience methods for retrieving the [[messages]] and [[size]].
  *
  * {{{
  * scala> errors.messages
  * res1: Vector[String] = Vector(error1, error2)
  *
  * scala> errors.size
  * res2: Int = 2
  * }}}
  *
  * You can also add more errors using the [[append]] method, creating a new [[ConfigErrors]].
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
  * @param toVector A non-empty Vector of [[ConfigError]] errors
  * @note the constructor is private to make sure all [[ConfigErrors]]
  *       have at least one [[ConfigError]] instance
  */
final class ConfigErrors private (val toVector: Vector[ConfigError]) extends AnyVal {

  /**
    * Appends a [[ConfigError]] by creating a new [[ConfigErrors]],
    * leaving this [[ConfigErrors]] unmodified. Errors are appended
    * in order, so the appended error will occur at the last position,
    * for example when using [[toVector]].
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
    * Returns the error messages of the errors for this [[ConfigErrors]].
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

  override def toString: String =
    s"ConfigErrors(${toVector.mkString(", ")})"
}

object ConfigErrors {

  /**
    * Creates a new [[ConfigErrors]] instance using the specified errors.
    *
    * @param first the first [[ConfigError]] error
    * @param rest any remaining [[ConfigError]] errors
    * @return a [[ConfigErrors]] instance with the specified errors
    * @example {{{
    * scala> ConfigErrors(ConfigError("error1"), ConfigError("error2"))
    * res0: ConfigErrors = ConfigErrors(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  def apply(first: ConfigError, rest: ConfigError*): ConfigErrors =
    new ConfigErrors(first +: rest.toVector)
}
