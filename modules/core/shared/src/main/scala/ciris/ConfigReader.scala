package ciris

import ciris.ConfigError.WrongType
import ciris.readers.ConfigReaders

import scala.util.{Failure, Success, Try}

abstract class ConfigReader[Value] { self =>
  def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, Value]

  final def map[A](f: Value => A): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        self.read(entry).fold(Left.apply, value => Right(f(value)))
    }

  final def mapOption[A](typeName: String)(f: Value => Option[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Some(b) => Right(b)
              case None => Left(WrongType(entry.key, value, typeName, entry.keyType))
            }
          })
    }

  final def mapEither[L, R](typeName: String)(f: Value => Either[L, R]): ConfigReader[R] =
    new ConfigReader[R] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, R] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Right(r) => Right(r)
              case Left(cause) =>
                Left(WrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
            }
          })
    }
}

object ConfigReader extends ConfigReaders {
  def apply[Value](implicit reader: ConfigReader[Value]): ConfigReader[Value] = reader

  def mapBoth[A](
    onError: ConfigError => Either[ConfigError, A],
    onValue: String => Either[ConfigError, A]
  ): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.fold(onError, onValue)
    }

  def map[A](f: String => Either[ConfigError, A]): ConfigReader[A] =
    mapBoth(Left.apply, f)

  def fromOption[A](typeName: String)(f: String => Option[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Some(t) => Right(t)
            case None => Left(WrongType(entry.key, value, typeName, entry.keyType))
          }
        }
    }

  def fromTry[A](typeName: String)(f: String => Try[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Success(a) => Right(a)
            case Failure(cause) =>
              Left(WrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }

  def catchNonFatal[A](typeName: String)(f: String => A): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          Try(f(value)) match {
            case Success(t) => Right(t)
            case Failure(cause) =>
              Left(WrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }
}
