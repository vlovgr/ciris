package ciris

import ciris.ConfigError.wrongType
import ciris.readers.ConfigReaders

import scala.util.{Failure, Success, Try}

/**
  * [[ConfigReader]] represents the ability to convert the value of a
  * [[ConfigSourceEntry]] (a value read from a [[ConfigSource]]) to a
  * different type. Values in a [[ConfigSourceEntry]] are always of
  * type `String`, so a [[ConfigReader]] provides a conversion from
  * `String` to `Value`, while supporting sensible error messages.
  *
  * To create a new [[ConfigReader]], simply extended the class and
  * implement the [[read]] method. Alternatively, use the methods
  * in the companion object, such as [[ConfigReader#identity]],
  * [[ConfigReader#fromOption]], and [[ConfigReader#fromTry]].
  *
  * @tparam Value the type to which the reader converts values
  */
abstract class ConfigReader[Value] { self =>

  /**
    * Reads the value of the specified [[ConfigSourceEntry]], converting
    * the value from type `String` to type `Value`, while supporting
    * sensible error messages.
    *
    * @param entry the [[ConfigSourceEntry]] from which to read the value
    * @tparam Key the type of the key read from the configuration source
    * @return the converted value or a [[ConfigError]] if reading failed
    */
  def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, Value]

  /**
    * Applies a function to the converted value from this [[ConfigReader]].
    * The specified function is only applied if the conversion to `Value`
    * is successful, otherwise the behaviour is the same.
    *
    * @param f the function to apply to the value if the read succeeded
    * @tparam A the return type of the specified function
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.map(_.take(2))
    * reader: ConfigReader[String] = ConfigReader$$$$anon$$1@2274f2dd
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,String] = Right(12)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,String] = Right(ab)
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def map[A](f: Value => A): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        self.read(entry).fold(Left.apply, value => Right(f(value)))
    }

  /**
    * Applies a function on the converted value, returning an `Option[A]`.
    * If the function returns `None`, the type conversion to `A` will have
    * been considered to have failed. Returning a `Some` means that the
    * conversion succeeded.
    *
    * @param typeName the name of the type `A`
    * @param f the function converting from `Value` to `Option[A]`
    * @tparam A the type for which to convert the value to
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.mapOption("Int")(value => scala.util.Try(value.toInt).toOption)
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$2@669d8d59
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, None))
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapOption[A](typeName: String)(f: Value => Option[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Some(b) => Right(b)
              case None => Left(wrongType(entry.key, value, typeName, entry.keyType))
            }
          })
    }

  /**
    * Applies a function on the converted value, returning an `Either[L, R]`.
    * If the function returns `Left[L, _]`, the type conversion to `R` will
    * be considered to have failed. Returning a `Right[_, R]` means that
    * the conversion succeeded.
    *
    * @param typeName the name of the type `R`
    * @param f the function converting from `Value` to `Either[L, R]`
    * @tparam L the type representing an error for the type conversion;<br>
    *           should have a sensible `toString` method for error messages
    * @tparam R the type for which to convert the value to
    * @return a new `ConfigReader[R]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.mapEither("Int")(value => scala.util.Try(value.toInt).fold(Left.apply, Right.apply))
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$3@8635c89
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, Some(java.lang.NumberFormatException: For input string: "abc")))
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapEither[L, R](typeName: String)(f: Value => Either[L, R]): ConfigReader[R] =
    new ConfigReader[R] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, R] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Right(r) => Right(r)
              case Left(cause) =>
                Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
            }
          })
    }
}

object ConfigReader extends ConfigReaders {
  def apply[Value](implicit reader: ConfigReader[Value]): ConfigReader[Value] = reader

  /**
    * A [[ConfigReader]] which does not modify the value read from a
    * configuration source. Most often not useful on its own, but can
    * be used as a starting point for other types of [[ConfigReader]]s.
    *
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity
    * reader: ConfigReader[String] = ConfigReader$$$$anon$$4@245c7250
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,String] = Right(123456)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,String] = Right(abc)
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  def identity: ConfigReader[String] =
    new ConfigReader[String] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, String] =
        entry.value
    }

  /**
    * Creates a new [[ConfigReader]] by applying a function for both the
    * error case and the value case, directly on the [[ConfigSourceEntry]],
    * without any intermediate type conversions.
    *
    * @param onError the function to apply in the case of an error
    * @param onValue the function to apply in case of a value
    * @tparam A the type of value in the specified functions
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.mapBoth(error => Left(error), value => Right(value + "/789"))
    * reader: ConfigReader[String] = ConfigReader$$$$anon$$4@76e4848f
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,String] = Right(123456/789)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,String] = Right(abc/789)
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  def mapBoth[A](
    onError: ConfigError => Either[ConfigError, A],
    onValue: String => Either[ConfigError, A]
  ): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.fold(onError, onValue)
    }

  /**
    * Creates a new [[ConfigReader]] by applying a function in the case
    * when a value was successfully read from the configuration source.
    *
    * @param f the function to apply in case of a value
    * @tparam A the type of value in the specified function
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.map(value => Right(value.take(2)))
    * reader: ConfigReader[String] = ConfigReader$$$$anon$$4@77f11239
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,String] = Right(12)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,String] = Left(MissingKey(1, Argument))
    * }}}
    */
  def map[A](f: String => Either[ConfigError, A]): ConfigReader[A] =
    mapBoth(Left.apply, f)

  /**
    * Creates a new [[ConfigReader]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * returning an `Option[A]`. If the function returns `None`, it will
    * be interpreted as if the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Option[A]`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "25"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.fromOption("String")(Some(_).filter(_.length == 1))
    * reader: ConfigReader[String] = ConfigReader$$$$anon$$5@4826b7ae
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,String] = Right(1)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,String] = Left(WrongType(1, 25, String, Argument, None))
    * }}}
    */
  def fromOption[A](typeName: String)(f: String => Option[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Some(t) => Right(t)
            case None => Left(wrongType(entry.key, value, typeName, entry.keyType))
          }
        }
    }

  /**
    * Creates a new [[ConfigReader]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * returning a `Try[A]`. If the function returns `Failure`, it will
    * be interpreted as if the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Try[A]`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.fromTry("Int")(value => scala.util.Try(value.toInt))
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$6@26db094b
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    * }}}
    */
  def fromTry[A](typeName: String)(f: String => Try[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Success(a) => Right(a)
            case Failure(cause) =>
              Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }

  /**
    * Creates a new [[ConfigReader]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * wrapping the function in a `Try`. If the function, for any
    * reason, throws an exception, it will be interpreted as if
    * the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `A`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.catchNonFatal("Int")(_.toInt)
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$7@4fee5a39
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    * }}}
    */
  def catchNonFatal[A](typeName: String)(f: String => A): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          Try(f(value)) match {
            case Success(t) => Right(t)
            case Failure(cause) =>
              Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }
}
