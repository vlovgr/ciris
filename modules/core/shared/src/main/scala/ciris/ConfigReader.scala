package ciris

import ciris.ConfigError.WrongType
import ciris.readers.ConfigReaders

import scala.util.{Failure, Success, Try}

sealed abstract class ConfigReader[A] { self ⇒
  def read(key: String)(implicit source: ConfigSource): Either[ConfigError, A]

  final def map[B](f: A ⇒ B): ConfigReader[B] =
    ConfigReader.pure { (key, source) ⇒
      self.read(key)(source).fold(Left.apply, value ⇒ Right(f(value)))
    }

  final def mapOption[B](typeName: String)(f: A ⇒ Option[B]): ConfigReader[B] =
    ConfigReader.pure { (key, source) ⇒
      self
        .read(key)(source)
        .fold(Left.apply, value ⇒ {
          f(value) match {
            case Some(b) ⇒ Right(b)
            case None ⇒ Left(WrongType(key, value, typeName, source.keyType))
          }
        })
    }

  final def mapEither[L, R](typeName: String)(f: A ⇒ Either[L, R]): ConfigReader[R] =
    ConfigReader.pure { (key, source) ⇒
      self
        .read(key)(source)
        .fold(Left.apply, value ⇒ {
          f(value) match {
            case Right(r) ⇒ Right(r)
            case Left(cause) ⇒ Left(WrongType(key, value, typeName, source.keyType, Some(cause)))
          }
        })
    }
}

object ConfigReader extends ConfigReaders {
  def apply[A](implicit reader: ConfigReader[A]): ConfigReader[A] = reader

  def pure[A](f: (String, ConfigSource) ⇒ Either[ConfigError, A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read(key: String)(implicit source: ConfigSource): Either[ConfigError, A] =
        f(key, source)
    }

  def fold[A](
    onMissingKey: ConfigError ⇒ Either[ConfigError, A],
    onValue: (String, String, ConfigSource) ⇒ Either[ConfigError, A]
  ): ConfigReader[A] = pure { (key, source) ⇒
    source.read(key).fold(onMissingKey, value ⇒ onValue(key, value, source))
  }

  def withValue[A](f: (String, String, ConfigSource) ⇒ Either[ConfigError, A]): ConfigReader[A] =
    fold(Left.apply, f)

  def fromOption[A](typeName: String)(f: String ⇒ Option[A]): ConfigReader[A] =
    withValue { (key, value, source) ⇒
      f(value) match {
        case Some(t) ⇒ Right(t)
        case None ⇒ Left(WrongType(key, value, typeName, source.keyType))
      }
    }

  def fromTry[A](typeName: String)(f: String ⇒ Try[A]): ConfigReader[A] =
    withValue { (key, value, source) ⇒
      f(value) match {
        case Success(a) ⇒ Right(a)
        case Failure(cause) ⇒ Left(WrongType(key, value, typeName, source.keyType, Some(cause)))
      }
    }

  def catchNonFatal[A](typeName: String)(f: String ⇒ A): ConfigReader[A] =
    withValue { (key, value, source) ⇒
      Try(f(value)) match {
        case Success(t) ⇒ Right(t)
        case Failure(cause) ⇒ Left(WrongType(key, value, typeName, source.keyType, Some(cause)))
      }
    }
}
