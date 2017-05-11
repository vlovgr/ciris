package ciris

import ciris.ConfigError.{MissingKey, ReadException}

import scala.util.{Failure, Success, Try}

sealed abstract class ConfigSource(val keyType: ConfigKeyType) {
  def read(key: String): Either[ConfigError, String]
}

object ConfigSource {
  def apply(keyType: ConfigKeyType)(read: String ⇒ Either[ConfigError, String]): ConfigSource = {
    val readValue = read
    new ConfigSource(keyType) {
      override def read(key: String): Either[ConfigError, String] = readValue(key)
      override def toString: String = s"ConfigSource($keyType)"
    }
  }

  def fromOption(keyType: ConfigKeyType)(read: String ⇒ Option[String]): ConfigSource =
    ConfigSource(keyType)(key ⇒
      read(key) match {
        case Some(value) ⇒ Right(value)
        case None ⇒ Left(MissingKey(key, keyType))
    })

  def fromMap(keyType: ConfigKeyType)(map: Map[String, String]): ConfigSource =
    ConfigSource.fromOption(keyType)(map.get)

  def fromTry(keyType: ConfigKeyType)(read: String ⇒ Try[String]): ConfigSource =
    ConfigSource(keyType)(key ⇒
      read(key) match {
        case Success(value) ⇒ Right(value)
        case Failure(cause) ⇒ Left(ReadException(key, keyType, cause))
    })

  def fromTryOption(keyType: ConfigKeyType)(read: String ⇒ Try[Option[String]]): ConfigSource =
    ConfigSource(keyType)(key ⇒
      read(key) match {
        case Success(Some(value)) ⇒ Right(value)
        case Success(None) ⇒ Left(MissingKey(key, keyType))
        case Failure(cause) ⇒ Left(ReadException(key, keyType, cause))
    })

  def catchNonFatal(keyType: ConfigKeyType)(read: String ⇒ String): ConfigSource =
    ConfigSource.fromTry(keyType)(key ⇒ Try(read(key)))

  case object Environment extends ConfigSource(ConfigKeyType.Environment) {
    override def read(key: String): Either[ConfigError, String] =
      ConfigSource.fromMap(keyType)(sys.env).read(key)
  }

  case object Properties extends ConfigSource(ConfigKeyType.Properties) {
    override def read(key: String): Either[ConfigError, String] =
      ConfigSource.fromTryOption(keyType)(key ⇒ Try(sys.props.get(key))).read(key)
  }
}
