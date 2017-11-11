package ciris

/**
  * A `Throwable` representation of [[ConfigErrors]]. Useful in cases
  * where it's desirable to have failed configuration loading halt
  * program execution - typically during application startup.
  *
  * @param errors the underlying [[ConfigErrors]] of the exception
  */
final class ConfigException private (val errors: ConfigErrors)
    extends Throwable({
      val errorMessages =
        errors.toVector
          .map(error => s"  - ${error.message}.")
          .mkString("\n", "\n", "\n")

      s"configuration loading failed with the following errors.\n$errorMessages"
    }) {

  override def toString: String =
    s"ciris.ConfigException: $getMessage"
}

object ConfigException {

  /**
    * Creates a new [[ConfigException]] using the specified [[ConfigErrors]].
    *
    * @param errors the [[ConfigErrors]] from which to create the exception
    * @return a new [[ConfigException]]
    */
  def apply(errors: ConfigErrors): ConfigException =
    new ConfigException(errors)
}
