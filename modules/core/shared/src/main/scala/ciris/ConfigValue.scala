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
      override def toString: String = s"ConfigValue($value)"
    }
  }
}
