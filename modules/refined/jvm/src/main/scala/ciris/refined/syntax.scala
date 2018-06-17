package ciris.refined

import ciris.api._
import ciris.api.syntax._
import ciris.{ConfigEntry, ConfigError}
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV

import scala.reflect.runtime.universe.WeakTypeTag

object syntax {
  implicit def refinedConfigEntrySyntax[F[_]: Monad, K, S, V](
    entry: ConfigEntry[F, K, S, V]
  ): RefinedConfigEntrySyntax[F, K, S, V] = {
    new RefinedConfigEntrySyntax(entry)
  }

  final class RefinedConfigEntrySyntax[F[_]: Monad, K, S, V] private[ciris] (
    val entry: ConfigEntry[F, K, S, V]
  ) {

    /**
      * Attempts to refine the value of the `ConfigEntry` by checking whether
      * the value conforms to the predicate `P`. If the value conforms to the
      * predicate, returns a new `ConfigEntry` with the refined value. If not,
      * returns a new `ConfigEntry` with an error detailing why the value does
      * not conform to the predicate.
      *
      * @param validate the implicit validate instance for the predicate
      * @param typeTag the implicit weak type tag for the refined type
      * @tparam P the type of the predicate; must be specified explicitly
      * @return a new `ConfigEntry` with the result of the refinement
      * @example {{{
      * scala> import ciris._, ciris.refined.syntax._, eu.timepit.refined.api.Refined, eu.timepit.refined.collection.NonEmpty
      * import ciris._
      * import ciris.refined.syntax._
      * import eu.timepit.refined.api.Refined
      * import eu.timepit.refined.collection.NonEmpty
      *
      * scala> val entry = ConfigEntry("key", ConfigKeyType.Property, Right("value"))
      * entry: ConfigEntry[api.Id, String, String, String] = ConfigEntry(key, Property)
      *
      * scala> entry.refineValue[NonEmpty]
      * res0: ConfigEntry[api.Id, String, String, Refined[String, NonEmpty]] = ConfigEntry(key, Property)
      * }}}
      */
    def refineValue[P](
      implicit validate: Validate[V, P],
      typeTag: WeakTypeTag[V Refined P]
    ): ConfigEntry[F, K, S, V Refined P] =
      entry.withValueF {
        for {
          sourceValue <- entry.sourceValue
          errorOrValue <- entry.value
        } yield {
          errorOrValue.right.flatMap { value =>
            refineV[P](value).fold(
              error => {
                val typeName = typeTag.tpe.toString
                Left(
                  ConfigError.wrongType(
                    entry.key,
                    entry.keyType,
                    sourceValue,
                    value,
                    typeName,
                    Some(error)
                  ))
              },
              refined => Right(refined)
            )
          }
        }
      }

    /**
      * Applies a function to the value of the `ConfigEntry` and attempts to
      * refine the returned value, by checking whether the value conforms to
      * the specified predicate `P`. Note that this function always gives
      * the same result as `entry.mapValue(f).refineValue[P]`.<br>
      * <br>
      * For this to work, we need to partially apply type parameters, so this
      * function returns an intermediate [[MapRefineValuePartiallyApplied]]
      * class for that purpose. However, you can simply use this function
      * like in the example.
      *
      * @tparam P the type of the predicate; must be specified
      * @return a new `ConfigEntry` with the result of the refinement
      * @example {{{
      * scala> import ciris._, ciris.refined.syntax._, eu.timepit.refined.api.Refined, eu.timepit.refined.string.Uri
      * import ciris._
      * import ciris.refined.syntax._
      * import eu.timepit.refined.api.Refined
      * import eu.timepit.refined.string.Uri
      *
      * scala> val host = ConfigEntry("key", ConfigKeyType.Property, Right("google.com"))
      * host: ConfigEntry[api.Id, String, String, String] = ConfigEntry(key, Property)
      *
      * scala> host.mapRefineValue[Uri](_ + "/api")
      * res0: ConfigEntry[api.Id, String, String, Refined[String, Uri]] = ConfigEntry(key, Property)
      * }}}
      */
    def mapRefineValue[P]: MapRefineValuePartiallyApplied[F, K, S, V, P] =
      new MapRefineValuePartiallyApplied(entry)
  }

  final class MapRefineValuePartiallyApplied[F[_]: Monad, K, S, V, P] private[ciris] (
    val entry: ConfigEntry[F, K, S, V]
  ) {

    def apply[A](f: V => A)(
      implicit validate: Validate[A, P],
      typeTag: WeakTypeTag[A Refined P]
    ): ConfigEntry[F, K, S, A Refined P] = {
      entry.mapValue(f).refineValue[P]
    }
  }
}
