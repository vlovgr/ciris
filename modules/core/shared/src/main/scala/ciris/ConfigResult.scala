package ciris

import ciris.api.Apply
import ciris.api.syntax._

/**
  * [[ConfigResult]] represents any result from having loaded a configuration:
  * either a single [[ConfigValue]] or multiple values composed together using
  * `loadConfig` or `withValues`. Most often, there is no need to create an
  * instance of [[ConfigResult]], instead prefer to use [[ConfigValue]].
  *
  * @tparam F the context in which the result exists
  * @tparam V the type of the result
  */
abstract class ConfigResult[F[_]: Apply, V] {
  def result: F[Either[ConfigErrors, V]]

  private[ciris] final def append[A](next: ConfigResult[F, A]): ConfigResult2[F, V, A] =
    new ConfigResult2((result product next.result).map {
      case (Right(v), Right(a))           => Right((v, a))
      case (Left(errors1), Right(_))      => Left(errors1)
      case (Right(_), Left(errors2))      => Left(errors2)
      case (Left(errors1), Left(errors2)) => Left(errors1 combine errors2)
    })

  override def toString: String =
    "ConfigResult$" + System.identityHashCode(this)

  /**
    * Returns a `String` representation of this [[ConfigResult]]
    * including the result. If the result is potentially sensitive,
    * then be careful to not include it in log output.
    *
    * @return a `String` representation with the result
    */
  def toStringWithResult: String =
    s"ConfigResult($result)"
}

object ConfigResult {

  /**
    * Creates a new [[ConfigResult]] from the specified result,
    * wrapped in context `F`, which can be [[api.Id]] if no
    * context is desired.
    *
    * @param result the result or errors, in context `F`
    * @tparam F the context in which the result exists
    * @tparam V the type of the result
    * @return a new [[ConfigResult]]
    */
  def apply[F[_]: Apply, V](result: F[Either[ConfigErrors, V]]): ConfigResult[F, V] = {
    val theResult = result
    new ConfigResult[F, V] {
      override def result: F[Either[ConfigErrors, V]] = theResult
    }
  }
}
