package ciris

import ciris.ConfigError.{InvalidKey, MissingKey}

import scala.util.{Failure, Success, Try}

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
      Try(sys.props.get(key)) match {
        case Success(Some(value)) ⇒
          Right(value)
        case Success(None) ⇒
          Left(MissingKey(key, this))
        case Failure(cause) ⇒
          Left(InvalidKey(key, this, cause))
      }

    override val keyType: String = "system property"
  }
}
