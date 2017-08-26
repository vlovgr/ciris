package ciris

object syntax {
  implicit def eitherConfigErrorsOps[T](
    either: Either[ConfigErrors, T]
  ): EitherConfigErrorsSyntax[T] = {
    new EitherConfigErrorsSyntax(either)
  }

  final class EitherConfigErrorsSyntax[T](val either: Either[ConfigErrors, T]) extends AnyVal {

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
}
