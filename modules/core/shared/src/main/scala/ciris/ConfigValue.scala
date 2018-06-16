package ciris

import ciris.api._
import ciris.api.syntax._

/**
  * [[ConfigValue]] represents the value part of a [[ConfigEntry]],
  * without any other details like the key, key type, or original
  * source value. [[ConfigEntry]] extends [[ConfigValue]], but
  * you can also create one with [[ConfigValue#apply]].
  *
  * @tparam F the context in which the value exists
  * @tparam V the type of the value
  */
abstract class ConfigValue[F[_]: Apply, V] {
  def value: F[Either[ConfigError, V]]

  /**
    * If the key for this value was missing from the [[ConfigSource]],
    * as determined by [[ConfigError.isMissingKey]], then uses `that`
    * [[ConfigValue]] instead. If `that` value is not available due
    * to an error, then the errors are accumulated.
    *
    * {{{
    * scala> val apiKey =
    *      |  env[String]("API_KEY").
    *      |    orElse(prop("api.key"))
    * apiKey: ConfigValue[api.Id, String] = ConfigValue(Left(Combined(MissingKey(API_KEY, Environment), MissingKey(api.key, Property))))
    *
    * scala> apiKey.value.left.map(_.message).toString
    * res0: String = Left(Missing environment variable [API_KEY] and missing system property [api.key])
    *
    * scala> env[String]("FILE_ENCODING").
    *      |   orElse(prop("file.encoding"))
    * res1: ConfigValue[api.Id, String] = ConfigValue(Right(UTF8))
    * }}}
    *
    * If the value is unavailable due to a different error than the
    * key missing from the [[ConfigSource]], then the alternative
    * value will not be used.
    *
    * {{{
    * scala> prop[Int]("file.encoding").
    *      |   orElse(env("FILE_ENCODING"))
    * res2: ConfigValue[api.Id, Int] = ConfigValue(Left(WrongType(file.encoding, Property, Right(UTF8), UTF8, Int, java.lang.NumberFormatException: For input string: "UTF8")))
    * }}}
    *
    * Note that the alternative value is passed by reference, and it
    * will only be evaluated if this [[ConfigValue]] is unavailable
    * due to the key missing from the [[ConfigSource]].
    *
    * @param that the [[ConfigValue]] to use if this value is unavailable
    *             due to the key missing from the [[ConfigSource]]
    * @return a new [[ConfigValue]]
    */
  final def orElse(that: => ConfigValue[F, V])(implicit m: Monad[F]): ConfigValue[F, V] =
    ConfigValue.applyF[F, V] {
      this.value.flatMap {
        case Left(thisError) if thisError.isMissingKey =>
          that.value.map {
            case right @ Right(_) => right
            case Left(thatError)  => Left(thisError combine thatError)
          }

        case other => other.pure[F]
      }
    }

  /**
    * If the key for this value was missing from the [[ConfigSource]],
    * as determined by [[ConfigError.isMissingKey]], then uses `None`
    * as value instead. If the value is available, it gets wrapped
    * in a `Some`.
    *
    * {{{
    * scala> env[String]("API_KEY").
    *      |   orElse(prop("api.key")).
    *      |   orNone
    * res0: ConfigValue[api.Id,Option[String]] = ConfigValue(Right(None))
    * }}}
    *
    * If the value is unavailable due to a different error than the
    * key missing from the [[ConfigSource]], then the error will
    * not be replaced with `None`.
    *
    * {{{
    * scala> prop[Int]("file.encoding").orNone
    * res1: ConfigValue[api.Id, Option[Int]] = ConfigValue(Left(WrongType(file.encoding, Property, Right(UTF8), UTF8, Int, java.lang.NumberFormatException: For input string: "UTF8")))
    * }}}
    *
    * @return a new [[ConfigValue]]
    */
  final def orNone: ConfigValue[F, Option[V]] =
    ConfigValue.applyF[F, Option[V]] {
      this.value.map {
        case Left(error) if error.isMissingKey => Right(None)
        case other                             => other.right.map(Some.apply)
      }
    }

  override def toString: String =
    "ConfigValue$" + System.identityHashCode(this)

  /**
    * Returns a [[String]] representation of this [[ConfigValue]]
    * including the value. If the value is potentially sensitive,
    * then be careful to not include it in log output.
    *
    * @return a [[String]] representation with the value
    */
  final def toStringWithValue: String =
    s"ConfigValue($value)"

  private[ciris] final def append[A](next: ConfigValue[F, A]): ConfigValue2[F, V, A] = {
    new ConfigValue2((this.value product next.value).map {
      case (Right(v), Right(a))         => Right((v, a))
      case (Left(error1), Right(_))     => Left(ConfigErrors(error1))
      case (Right(_), Left(error2))     => Left(ConfigErrors(error2))
      case (Left(error1), Left(error2)) => Left(error1 append error2)
    })
  }
}

object ConfigValue {

  /**
    * Creates a new [[ConfigValue]] from the specified value.
    *
    * @param value the value or an error
    * @tparam V the type of the value
    * @return a new [[ConfigValue]]
    */
  def apply[V](value: Either[ConfigError, V]): ConfigValue[Id, V] =
    ConfigValue.applyF[Id, V](value)

  /**
    * Creates a new [[ConfigValue]] from the specified value,
    * wrapped in context `F`, which can be [[api.Id]] if no
    * context is desired. [[ConfigValue#apply]] also exists
    * for the case when `F` is [[api.Id]].
    *
    * @param value the value or an error, in context `F`
    * @tparam F the context in which the value exists
    * @tparam V the type of the value
    * @return a new [[ConfigValue]]
    */
  def applyF[F[_]: Apply, V](value: F[Either[ConfigError, V]]): ConfigValue[F, V] = {
    val theValue = value
    new ConfigValue[F, V] {
      override def value: F[Either[ConfigError, V]] = theValue
    }
  }
}
