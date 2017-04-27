package ciris

import ciris.ConfigError.MissingKey

abstract class ConfigSource {
  def read(key: String): Either[ConfigError, String]
  def keyType: String
}

object ConfigSource {
  case object Environment extends ConfigSource {
    override def read(key: String): Either[ConfigError, String] =
      sys.env.get(key).map(Right(_)).getOrElse(Left(MissingKey(key, this)))

    override val keyType: String = "environment variable"
  }

  case object Properties extends ConfigSource {
    override def read(key: String): Either[ConfigError, String] =
      sys.props.get(key).map(Right(_)).getOrElse(Left(MissingKey(key, this)))

    override val keyType: String = "system property"
  }
}
