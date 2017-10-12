package ciris.refined

import ciris.{ConfigError, ConfigValue}
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV

object syntax {
  implicit def refinedConfigValueSyntax[T](
    config: ConfigValue[T]
  ): RefinedConfigValueSyntax[T] = {
    new RefinedConfigValueSyntax[T](config)
  }

  final class RefinedConfigValueSyntax[T](val config: ConfigValue[T]) {

    /**
      * Attempts to refine the [[ConfigValue]] by checking whether the
      * value conforms to the predicate `P`. If the value conforms to
      * the predicate, returns a new [[ConfigValue]] with the refined
      * value; otherwise, returns a new [[ConfigValue]] with an error
      * detailing why the value does not conform to the predicate.
      *
      * @param validate the implicit validate instance for the predicate
      * @tparam P the type of the predicate, must be specified
      * @return a new [[ConfigValue]] with the result of the refinement
      * @example {{{
      * scala> import ciris._, ciris.refined.syntax._, eu.timepit.refined.api.Refined, eu.timepit.refined.collection.NonEmpty
      * import ciris._
      * import ciris.refined.syntax._
      * import eu.timepit.refined.api.Refined
      * import eu.timepit.refined.collection.NonEmpty
      *
      * scala> val secret = ConfigValue(Right("secret")).refine[NonEmpty]
      * secret: ConfigValue[Refined[String,NonEmpty]] = ConfigValue(Right(secret))
      * }}}
      */
    def refine[P](implicit validate: Validate[T, P]): ConfigValue[Refined[T, P]] =
      config.flatMap { t =>
        refineV[P](t).fold(
          error => Left(ConfigError(s"Unable to refine value [$t]: $error")),
          refined => Right(refined)
        )
      }

    /**
      * Applies a function to the [[ConfigValue]] and attempts to refine
      * the returned value, by checking whether the value conforms to the
      * specified predicate `P`.
      *
      * For this to work, we need to partially apply type parameters, so
      * this function returns an intermediate [[MapRefinePartiallyApplied]]
      * class for that purpose. However, you can simply use this function
      * like in the following example.
      *
      * @tparam P the type of the predicate, must be specified
      * @return a new [[ConfigValue]] with the result of the refinement
      * @example {{{
      * scala> import ciris._, ciris.refined.syntax._, eu.timepit.refined.api.Refined, eu.timepit.refined.string.Uri
      * import ciris._
      * import ciris.refined.syntax._
      * import eu.timepit.refined.api.Refined
      * import eu.timepit.refined.string.Uri
      *
      * scala> val host = ConfigValue(Right("google.com"))
      * host: ConfigValue[String] = ConfigValue(Right(google.com))
      *
      * scala> val api = host.mapRefine[Uri](_ + "/api")
      * api: ConfigValue[Refined[String,Uri]] = ConfigValue(Right(google.com/api))
      * }}}
      */
    def mapRefine[P]: MapRefinePartiallyApplied[T, P] =
      new MapRefinePartiallyApplied[T, P](config)
  }

  final class MapRefinePartiallyApplied[T, P](val config: ConfigValue[T]) {
    def apply[S](f: T => S)(implicit validate: Validate[S, P]): ConfigValue[Refined[S, P]] =
      config.flatMap { t =>
        val s = f(t)
        refineV[P](s).fold(
          error =>
            Left(ConfigError(s"Converted value [$t] to [$s] but was unable to refine: $error")),
          refined => Right(refined)
        )
      }
  }
}
