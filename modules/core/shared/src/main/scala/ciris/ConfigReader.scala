package ciris

import ciris.ConfigError.wrongType
import ciris.readers.ConfigReaders

import scala.util.{Failure, Success, Try}

/**
  * [[ConfigReader]] represents the ability to convert the value of a
  * [[ConfigEntry]] (a value read from a [[ConfigSource]]) to a
  * different type. Values in a [[ConfigEntry]] need to be of
  * type `String` to be supported, so a [[ConfigReader]] provides
  * a conversion from `String` to `Value`, while supporting
  * sensible error messages.
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
    * Reads the value of the specified [[ConfigEntry]], converting
    * the value from type `String` to type `Value`, while supporting
    * sensible error messages.
    *
    * @param entry the [[ConfigEntry]] from which to read the value
    * @tparam Key the type of the key read from the configuration source
    * @return the converted value or a [[ConfigError]] if reading failed
    */
  def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, Value]

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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Some(a) => Right(a)
              case None    => Left(wrongType(entry.key, value, typeName, entry.keyType))
            }
          })
    }

  /**
    * Applies a function on the converted value, returning a `Try[A]`.
    * If the function returns a `Success`, the type conversion will
    * be considered successful. Returning a `Failure` means that
    * the conversion failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function converting from `Value` to `Try[A]`
    * @tparam A the type for which to convert the value to
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.mapTry("Int")(value => scala.util.Try(value.toInt))
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$3@380729e4
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
  final def mapTry[A](typeName: String)(f: Value => Try[A]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        self
          .read(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Success(a) => Right(a)
              case Failure(cause) =>
                Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
            }
          })
    }

  /**
    * Applies a function on the converted value to `A`, making sure to catch
    * any non-fatal exceptions thrown by the function. The conversion will
    * be considered successful only if the function does not throw an
    * exception.
    *
    * @param typeName the name of the type `A`
    * @param f the function converting from `Value` to `A`
    * @tparam A the type for which to convert the value to
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.mapCatchNonFatal("Int")(_.toInt)
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$3@17323c05
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
  final def mapCatchNonFatal[A](typeName: String)(f: Value => A): ConfigReader[A] =
    mapTry(typeName)(value => Try(f(value)))

  /**
    * Applies a partial function on the converted value. The type conversion to
    * `A` will only succeed for values which the partial function is defined.
    *
    * @param typeName the name of the type `A`
    * @param f the partial function converting from `Value` to `A`
    * @tparam A the type for which to convert the value to
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "-123"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.identity.collect("PosBigInt") { case s if s.forall(_.isDigit) => BigInt(s) }
    * reader: ConfigReader[scala.math.BigInt] = ConfigReader$$$$anon$$2@727cfc59
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,scala.math.BigInt] = Right(123456)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,scala.math.BigInt] = Left(WrongType(1, -123, PosBigInt, Argument, None))
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,scala.math.BigInt] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def collect[A](typeName: String)(f: PartialFunction[Value, A]): ConfigReader[A] =
    mapOption(typeName) {
      case value if f.isDefinedAt(value) =>
        Some(f(value))
      case _ =>
        None
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
    * scala> val reader = ConfigReader.identity.mapEither("Int")(value => scala.util.Try(value.toInt).map(Right.apply).recover { case e => Left(e) }.get)
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, R] =
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

  /**
    * Applies a function on the values in the [[ConfigEntry]]s read by
    * the reader, before trying to convert the value to type `Value`. This
    * returns a new [[ConfigReader]] with the behavior, leaving the
    * current [[ConfigReader]] unmodified.
    *
    * @param f the function to apply on the [[ConfigEntry]] values
    * @return a new [[ConfigReader]] with the entry value transformation
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123 "))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader[Int].mapEntryValue(_.trim)
    * reader: ConfigReader[Int] = ciris.ConfigReader$$$$anon$$5@57c04ac9
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123)
    * }}}
    */
  final def mapEntryValue(f: String => String): ConfigReader[Value] =
    new ConfigReader[Value] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, Value] =
        self.read(entry.mapValueRight(f))
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, String] =
        entry.value
    }

  /**
    * Creates a new [[ConfigReader]] by applying a function for both the
    * error case and the value case, directly on the [[ConfigEntry]],
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Some(t) => Right(t)
            case None    => Left(wrongType(entry.key, value, typeName, entry.keyType))
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
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
    * returning a `Try[Option[A]]`. The conversion will only succeed
    * if the function returns `Success[Some[A]]`.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Try[Option[A]]`
    * @tparam A the type to convert to
    * @return a new `ConfigReader[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "1234", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val reader = ConfigReader.fromTryOption("Int")(value => scala.util.Try(if(value.length < 4) Some(value.toInt) else None))
    * reader: ConfigReader[Int] = ConfigReader$$$$anon$$9@7d20803b
    *
    * scala> reader.read(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> reader.read(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, 1234, Int, Argument, None))
    *
    * scala> reader.read(source.read(2))
    * res2: Either[ConfigError,Int] = Left(WrongType(2, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    *
    * scala> reader.read(source.read(3))
    * res3: Either[ConfigError,Int] = Left(MissingKey(3, Argument))
    * }}}
    */
  def fromTryOption[A](typeName: String)(f: String => Try[Option[A]]): ConfigReader[A] =
    new ConfigReader[A] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Success(Some(value)) => Right(value)
            case Success(None)        => Left(wrongType(entry.key, value, typeName, entry.keyType))
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
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          Try(f(value)) match {
            case Success(t) => Right(t)
            case Failure(cause) =>
              Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }
}
