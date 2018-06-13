package ciris

import ciris.api.MonadError
import ciris.api.syntax._

object syntax {
  implicit def eitherConfigErrorsSyntax[T](
    either: Either[ConfigErrors, T]
  ): EitherConfigErrorsSyntax[T] = {
    new EitherConfigErrorsSyntax(either)
  }

  final class EitherConfigErrorsSyntax[T](
    val either: Either[ConfigErrors, T]
  ) extends AnyVal {

    /**
      * If the configuration was loaded successfully, returns the
      * configuration; otherwise, an exception is thrown with a
      * message detailing why the the loading failed.
      *
      * @return the configuration, or an exception if the
      *         configuration failed to load
      */
    def orThrow(): T =
      either.fold(
        errors => throw errors.toException,
        identity
      )
  }

  implicit def eitherConfigErrorsFSyntax[F[_], T](
    eitherF: F[Either[ConfigErrors, T]]
  ): EitherConfigErrorsFSyntax[F, T] = {
    new EitherConfigErrorsFSyntax(eitherF)
  }

  final class EitherConfigErrorsFSyntax[F[_], T](
    val eitherF: F[Either[ConfigErrors, T]]
  ) extends AnyVal {

    /**
      * If the configuration was loaded successfully, returns the
      * configuration in the context `F`; otherwise, `ConfigErrors`
      * will be raised as an error in the context `F`.
      *
      * @param F the context in which the configuration was loaded
      * @return the configuration in context `F`, or an `F` were
      *         errors during configuration loading has been
      *         raised as an error
      */
    def orRaiseErrors(implicit F: MonadError[F, ConfigErrors]): F[T] =
      eitherF.flatMap {
        case Right(t) => F.pure(t)
        case Left(e)  => F.raiseError(e)
      }

    /**
      * If the configuration was loaded successfully, returns the
      * configuration in the context `F`; otherwise, `Throwable`
      * will be raised as an error in the context `F`.
      *
      * @param F the context in which the configuration was loaded
      * @return the configuration in context `F`, or an `F` were
      *         errors during configuration loading has been
      *         raised as an error
      */
    def orRaiseThrowable(implicit F: MonadError[F, Throwable]): F[T] =
      eitherF.flatMap {
        case Right(t) => F.pure(t)
        case Left(e)  => F.raiseError(e.toException)
      }
  }
}
