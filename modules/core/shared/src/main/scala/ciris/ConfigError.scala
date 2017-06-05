package ciris

sealed abstract class ConfigError {
  def message: String

  final def combine(error: ConfigError): ConfigError =
    ConfigError.combined(this, error)

  final def append(error: ConfigError): ConfigErrors =
    ConfigErrors(this) append error
}

object ConfigError {
  def apply(message: String): ConfigError = {
    val theMessage = message
    new ConfigError {
      override val message: String = theMessage
      override def toString: String = s"ConfigError($message)"
    }
  }

  sealed abstract case class Combined(errors: Vector[ConfigError]) extends ConfigError {
    override def message: String = errors.map(_.message).mkString(", ")
  }

  def combined(error1: ConfigError, error2: ConfigError, rest: ConfigError*): Combined =
    new Combined(Vector(error1, error2) ++ rest) {}

  final case class MissingKey(key: String, keyType: ConfigKeyType) extends ConfigError {
    override def message: String = s"Missing ${keyType.name} [$key]"
  }

  final case class ReadException(key: String, keyType: ConfigKeyType, cause: Throwable)
      extends ConfigError {

    override def message: String = s"Exception while reading ${keyType.name} [$key]: $cause"
  }

  final case class WrongType[A, B](
    key: String,
    value: A,
    typeName: String,
    keyType: ConfigKeyType,
    cause: Option[B] = None
  ) extends ConfigError {
    override def message: String = {
      val causeMessage = cause.map(cause => s": $cause").getOrElse("")
      s"${keyType.name.capitalize} [$key] with value [$value] cannot be converted to type [$typeName]$causeMessage"
    }
  }
}
