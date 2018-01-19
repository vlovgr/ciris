package ciris

import ciris.ConfigError.wrongType
import ciris.decoders.ConfigDecoders

import scala.util.{Failure, Success, Try}

/**
  * [[ConfigDecoder]] represents the ability to convert the value of a
  * [[ConfigEntry]] (an entry read from a [[ConfigSource]]) to a
  * different type. Values in a [[ConfigEntry]] need to be of
  * type `String` to be supported, so a [[ConfigDecoder]] provides
  * a conversion from `String` to `Value`, while supporting
  * sensible error messages.
  *
  * To create a new [[ConfigDecoder]], simply extended the class and
  * implement the [[decode]] method. Alternatively, use the methods
  * in the companion object, such as [[ConfigDecoder#identity]],
  * [[ConfigDecoder#fromOption]], and [[ConfigDecoder#fromTry]].
  *
  * @tparam Value the type to which the decoder converts values
  */
abstract class ConfigDecoder[Value] { self =>

  /**
    * Decodes the value of the specified [[ConfigEntry]], converting
    * the value from type `String` to type `Value`, while supporting
    * sensible error messages.
    *
    * @param entry the [[ConfigEntry]] from which to decode the value
    * @tparam K the type of the key read from the configuration source
    * @tparam S the type of the original configuration source value
    * @return the decoded value or a [[ConfigError]] if decoding failed
    */
  def decode[K, S](
    entry: ConfigEntry[K, S, String]
  ): Either[ConfigError, Value]

  /**
    * Applies a function to the converted value from this [[ConfigDecoder]].
    * The specified function is only applied if the conversion to `Value`
    * is successful, otherwise the behaviour is the same.
    *
    * @param f the function to apply to the value if the decode succeeded
    * @tparam A the return type of the specified function
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.map(_.take(2))
    * decoder: ConfigDecoder[String] = ConfigDecoder$$$$anon$$1@2274f2dd
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,String] = Right(12)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,String] = Right(ab)
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def map[A](f: Value => A): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        self.decode(entry).fold(Left.apply, value => Right(f(value)))
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
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.mapOption("Int")(value => scala.util.Try(value.toInt).toOption)
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$2@669d8d59
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, None))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapOption[A](typeName: String)(f: Value => Option[A]): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        self
          .decode(entry)
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
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.mapTry("Int")(value => scala.util.Try(value.toInt))
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$3@380729e4
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, Some(java.lang.NumberFormatException: For input string: "abc")))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapTry[A](typeName: String)(f: Value => Try[A]): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        self
          .decode(entry)
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
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.mapCatchNonFatal("Int")(_.toInt)
    * decoder: ConfigDecoder[Int] = ConfigDecoder@17323c05
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, Some(java.lang.NumberFormatException: For input string: "abc")))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapCatchNonFatal[A](typeName: String)(f: Value => A): ConfigDecoder[A] =
    mapTry(typeName)(value => Try(f(value)))

  /**
    * Applies a partial function on the converted value. The type conversion to
    * `A` will only succeed for values which the partial function is defined.
    *
    * @param typeName the name of the type `A`
    * @param f the partial function converting from `Value` to `A`
    * @tparam A the type for which to convert the value to
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "-123"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.collect("PosBigInt") { case s if s.forall(_.isDigit) => BigInt(s) }
    * decoder: ConfigDecoder[scala.math.BigInt] = ConfigDecoder$$$$anon$$2@727cfc59
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,scala.math.BigInt] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,scala.math.BigInt] = Left(WrongType(1, -123, PosBigInt, Argument, None))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,scala.math.BigInt] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def collect[A](typeName: String)(f: PartialFunction[Value, A]): ConfigDecoder[A] =
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
    * @return a new `ConfigDecoder[R]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity.mapEither("Int")(value => scala.util.Try(value.toInt).map(Right.apply).recover { case e => Left(e) }.get)
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$3@8635c89
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, abc, Int, Argument, Some(java.lang.NumberFormatException: For input string: "abc")))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,Int] = Left(MissingKey(2, Argument))
    * }}}
    */
  final def mapEither[L, R](typeName: String)(f: Value => Either[L, R]): ConfigDecoder[R] =
    new ConfigDecoder[R] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, R] =
        self
          .decode(entry)
          .fold(Left.apply, value => {
            f(value) match {
              case Right(r) => Right(r)
              case Left(cause) =>
                Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
            }
          })
    }

  /**
    * Applies a function on the values in the [[ConfigEntry]]s decoded by
    * the decoder, before trying to convert the value to type `Value`.
    * This returns a new [[ConfigDecoder]] with the behavior, leaving
    * the current [[ConfigDecoder]] unmodified.
    *
    * @param f the function to apply on the [[ConfigEntry]] values
    * @return a new [[ConfigDecoder]] with the entry value transformation
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123 "))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder[Int].mapEntryValue(_.trim)
    * decoder: ConfigDecoder[Int] = ciris.ConfigDecoder$$$$anon$$5@57c04ac9
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(123)
    * }}}
    */
  final def mapEntryValue(f: String => String): ConfigDecoder[Value] =
    new ConfigDecoder[Value] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, Value] =
        self.decode(entry.mapValueRight(f))
    }
}

object ConfigDecoder extends ConfigDecoders {
  def apply[Value](implicit decoder: ConfigDecoder[Value]): ConfigDecoder[Value] = decoder

  /**
    * A [[ConfigDecoder]] which does not modify the value read from a
    * configuration source. Most often not useful on its own, but can
    * be used as a starting point for other types of [[ConfigDecoder]]s.
    *
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.identity
    * decoder: ConfigDecoder[String] = ConfigDecoder$$$$anon$$4@245c7250
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,String] = Right(123456)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,String] = Right(abc)
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  def identity: ConfigDecoder[String] =
    new ConfigDecoder[String] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, String] =
        entry.value
    }

  /**
    * Creates a new [[ConfigDecoder]] by applying a function for both the
    * error case and the value case, directly on the [[ConfigEntry]],
    * without any intermediate type conversions.
    *
    * @param onError the function to apply in the case of an error
    * @param onValue the function to apply in case of a value
    * @tparam A the type of value in the specified functions
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456", "abc"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.mapBoth(error => Left(error), value => Right(value + "/789"))
    * decoder: ConfigDecoder[String] = ConfigDecoder$$$$anon$$4@76e4848f
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,String] = Right(123456/789)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,String] = Right(abc/789)
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,String] = Left(MissingKey(2, Argument))
    * }}}
    */
  def mapBoth[A](
    onError: ConfigError => Either[ConfigError, A],
    onValue: String => Either[ConfigError, A]
  ): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.fold(onError, onValue)
    }

  /**
    * Creates a new [[ConfigDecoder]] by applying a function in the case
    * when a value was successfully read from the configuration source.
    *
    * @param f the function to apply in case of a value
    * @tparam A the type of value in the specified function
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("123456"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.map(value => Right(value.take(2)))
    * decoder: ConfigDecoder[String] = ConfigDecoder$$$$anon$$4@77f11239
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,String] = Right(12)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,String] = Left(MissingKey(1, Argument))
    * }}}
    */
  def map[A](f: String => Either[ConfigError, A]): ConfigDecoder[A] =
    mapBoth(Left.apply, f)

  /**
    * Creates a new [[ConfigDecoder]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * returning an `Option[A]`. If the function returns `None`, it will
    * be interpreted as if the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Option[A]`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "25"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.fromOption("String")(Some(_).filter(_.length == 1))
    * decoder: ConfigDecoder[String] = ConfigDecoder$$$$anon$$5@4826b7ae
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,String] = Right(1)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,String] = Left(WrongType(1, 25, String, Argument, None))
    * }}}
    */
  def fromOption[A](typeName: String)(f: String => Option[A]): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Some(t) => Right(t)
            case None    => Left(wrongType(entry.key, value, typeName, entry.keyType))
          }
        }
    }

  /**
    * Creates a new [[ConfigDecoder]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * returning a `Try[A]`. If the function returns `Failure`, it will
    * be interpreted as if the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Try[A]`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.fromTry("Int")(value => scala.util.Try(value.toInt))
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$6@26db094b
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    * }}}
    */
  def fromTry[A](typeName: String)(f: String => Try[A]): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          f(value) match {
            case Success(a) => Right(a)
            case Failure(cause) =>
              Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }

  /**
    * Creates a new [[ConfigDecoder]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * returning a `Try[Option[A]]`. The conversion will only succeed
    * if the function returns `Success[Some[A]]`.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `Try[Option[A]]`
    * @tparam A the type to convert to
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "1234", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.fromTryOption("Int")(value => scala.util.Try(if(value.length < 4) Some(value.toInt) else None))
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$9@7d20803b
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, 1234, Int, Argument, None))
    *
    * scala> decoder.decode(source.read(2))
    * res2: Either[ConfigError,Int] = Left(WrongType(2, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    *
    * scala> decoder.decode(source.read(3))
    * res3: Either[ConfigError,Int] = Left(MissingKey(3, Argument))
    * }}}
    */
  def fromTryOption[A](typeName: String)(f: String => Try[Option[A]]): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
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
    * Creates a new [[ConfigDecoder]] by applying a function in the case
    * when a value was successfully read from the configuration source,
    * wrapping the function in a `Try`. If the function, for any
    * reason, throws an exception, it will be interpreted as if
    * the conversion to type `A` failed.
    *
    * @param typeName the name of the type `A`
    * @param f the function to apply on the value, returning `A`
    * @tparam A the type of value in the specified function
    * @return a new `ConfigDecoder[A]`
    * @example {{{
    * scala> val source = ConfigSource.byIndex(ConfigKeyType.Argument)(Vector("1", "a"))
    * source: ConfigSource[Int] = ConfigSource(Argument)
    *
    * scala> val decoder = ConfigDecoder.catchNonFatal("Int")(_.toInt)
    * decoder: ConfigDecoder[Int] = ConfigDecoder$$$$anon$$7@4fee5a39
    *
    * scala> decoder.decode(source.read(0))
    * res0: Either[ConfigError,Int] = Right(1)
    *
    * scala> decoder.decode(source.read(1))
    * res1: Either[ConfigError,Int] = Left(WrongType(1, a, Int, Argument, Some(java.lang.NumberFormatException: For input string: "a")))
    * }}}
    */
  def catchNonFatal[A](typeName: String)(f: String => A): ConfigDecoder[A] =
    new ConfigDecoder[A] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A] =
        entry.value.right.flatMap { value =>
          Try(f(value)) match {
            case Success(t) => Right(t)
            case Failure(cause) =>
              Left(wrongType(entry.key, value, typeName, entry.keyType, Some(cause)))
          }
        }
    }
}
