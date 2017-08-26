package ciris

/**
  * A `Throwable` representation of [[ConfigErrors]]. Useful in cases
  * where it's desirable to have failed configuration loading halt
  * program execution - typically during application startup.<br>
  * <br>
  * There's a quick way to convert [[ConfigErrors]] to an exception.
  * {{{
  * scala> ConfigErrors(ConfigError("error1")).toException
  * res0: ConfigException = ConfigException(ConfigError(error1))
  * }}}
  *
  * @param errors the underlying [[ConfigErrors]] of the exception
  */
final class ConfigException private (errors: ConfigErrors)
    extends Throwable({
      val errorCount =
        if (errors.size == 1) "error"
        else s"[${errors.size}] errors"

      val errorMessages =
        errors.toVector
          .map(error => s"  - ${error.message}.")
          .mkString("\n", "\n", "\n")

      s"configuration loading failed with the following $errorCount.\n$errorMessages"
    }) {

  override def toString: String =
    s"ConfigException(${errors.toVector.mkString(", ")})"
}

object ConfigException {

  /**
    * Creates a new [[ConfigException]] using the specified [[ConfigErrors]].
    *
    * @param errors the [[ConfigErrors]] from which to create the exception
    * @return a new [[ConfigException]]
    * @example {{{
    * scala> ConfigException(ConfigError("error1") append ConfigError("error2"))
    * res0: ConfigException = ConfigException(ConfigError(error1), ConfigError(error2))
    *
    * scala> (ConfigError("error1") append ConfigError("error2")).toException
    * res1: ConfigException = ConfigException(ConfigError(error1), ConfigError(error2))
    * }}}
    */
  def apply(errors: ConfigErrors): ConfigException =
    new ConfigException(errors)
}
