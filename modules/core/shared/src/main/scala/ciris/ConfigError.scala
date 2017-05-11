package ciris

sealed abstract class ConfigError {
  def message: String

  final def append(error: ConfigError): ConfigErrors =
    ConfigErrors(this) append error
}

object ConfigError {
  final case class MissingKey(key: String, keyType: ConfigKeyType) extends ConfigError {
    override def message: String = s"Missing ${keyType.value} [$key]"
  }

  final case class ReadException(key: String, keyType: ConfigKeyType, cause: Throwable)
      extends ConfigError {

    override def message: String = s"Exception while reading ${keyType.value} [$key]: $cause"
  }

  final case class WrongType[A, B](
    key: String,
    value: A,
    typeName: String,
    keyType: ConfigKeyType,
    cause: Option[B] = None
  ) extends ConfigError {
    override def message: String = {
      val causeMessage = cause.map(cause ⇒ s": $cause").getOrElse("")
      s"${keyType.value.capitalize} [$key] with value [$value] cannot be converted to type [$typeName]$causeMessage"
    }
  }
}
