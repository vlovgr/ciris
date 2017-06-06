package ciris

import ciris.ConfigError.{MissingKey, ReadException}

import scala.util.{Failure, Success, Try}

abstract class ConfigSource[Key](val keyType: ConfigKeyType[Key]) {
  def read(key: Key): ConfigSourceEntry[Key]
}

object ConfigSource {
  def apply[Key](keyType: ConfigKeyType[Key])(read: Key => Either[ConfigError, String]): ConfigSource[Key] = {
    val entry = (key: Key) => ConfigSourceEntry(key, keyType, read(key))
    new ConfigSource(keyType) {
      override def read(key: Key): ConfigSourceEntry[Key] = entry(key)
      override def toString: String = s"ConfigSource($keyType)"
    }
  }

  def fromOption[Key](keyType: ConfigKeyType[Key])(read: Key => Option[String]): ConfigSource[Key] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Some(value) => Right(value)
        case None => Left(MissingKey(key, keyType))
    })

  def fromMap[Key](keyType: ConfigKeyType[Key])(map: Map[Key, String]): ConfigSource[Key] =
    ConfigSource.fromOption(keyType)(map.get)

  def fromTry[Key](keyType: ConfigKeyType[Key])(read: Key => Try[String]): ConfigSource[Key] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Success(value) => Right(value)
        case Failure(cause) => Left(ReadException(key, keyType, cause))
    })

  def fromTryOption[Key](keyType: ConfigKeyType[Key])(read: Key => Try[Option[String]]): ConfigSource[Key] =
    ConfigSource(keyType)(key =>
      read(key) match {
        case Success(Some(value)) => Right(value)
        case Success(None) => Left(MissingKey(key, keyType))
        case Failure(cause) => Left(ReadException(key, keyType, cause))
    })

  def catchNonFatal[Key](keyType: ConfigKeyType[Key])(read: Key => String): ConfigSource[Key] =
    ConfigSource.fromTry(keyType)(key => Try(read(key)))

  case object Environment extends ConfigSource[String](ConfigKeyType.Environment) {
    val delegate: ConfigSource[String] =
      ConfigSource.fromMap(keyType)(sys.env)

    override def read(key: String): ConfigSourceEntry[String] =
      delegate.read(key)
  }

  case object Properties extends ConfigSource[String](ConfigKeyType.Properties) {
    val delegate: ConfigSource[String] =
      ConfigSource.fromTryOption(keyType)(key => Try(sys.props.get(key)))

    override def read(key: String): ConfigSourceEntry[String] =
      delegate.read(key)
  }
}
