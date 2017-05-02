package ciris

sealed abstract class ConfigError {
  def message: String

  final def append(error: ConfigError): ConfigErrors =
    ConfigErrors(this) append error
}

object ConfigError {
  final case class MissingKey(key: String, source: ConfigSource) extends ConfigError {
    override def message: String = s"Missing ${source.keyType} [$key]"
  }

  final case class InvalidKey(key: String, source: ConfigSource, cause: Throwable)
      extends ConfigError {

    override def message: String = s"Invalid ${source.keyType} [$key]: $cause"
  }

  final case class WrongType[A, B](
    key: String,
    value: A,
    typeName: String,
    source: ConfigSource,
    cause: Option[B] = None
  ) extends ConfigError {
    override def message: String = {
      val causeMessage = cause.map(cause ⇒ s": $cause").getOrElse("")
      s"${source.keyType.capitalize} [$key] with value [$value] cannot be converted to type [$typeName]$causeMessage"
    }
  }
}
