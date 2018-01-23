package ciris.refined

import ciris.{ConfigEntry, ConfigError}
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV

import scala.reflect.runtime.universe.WeakTypeTag

object syntax {
  implicit def refinedConfigEntrySyntax[K, S, V](
    entry: ConfigEntry[K, S, V]
  ): RefinedConfigEntrySyntax[K, S, V] = {
    new RefinedConfigEntrySyntax[K, S, V](entry)
  }

  final class RefinedConfigEntrySyntax[K, S, V] private[ciris] (val entry: ConfigEntry[K, S, V])
      extends AnyVal {

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
      * entry: ConfigEntry[String, String, String] = ConfigEntry(key, Property, Right(value))
      *
      * scala> entry.refineValue[NonEmpty]
      * res0: ConfigEntry[String, String, Refined[String, NonEmpty]] = ConfigEntry(key, Property, Right(value))
      * }}}
      */
    def refineValue[P](
      implicit validate: Validate[V, P],
      typeTag: WeakTypeTag[V Refined P]
    ): ConfigEntry[K, S, V Refined P] =
      entry.flatMapValue { value =>
        refineV[P](value).fold(
          error => {
            val typeName = typeTag.tpe.toString
            Left(
              ConfigError.wrongType(
                entry.key,
                entry.keyType,
                entry.sourceValue,
                value,
                typeName,
                Some(error)
              ))
          },
          refined => Right(refined)
        )
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
      * host: ConfigEntry[String, String, String] = ConfigEntry(key, Property, Right(google.com))
      *
      * scala> val api = host.mapRefineValue[Uri](_ + "/api")
      * api: ConfigEntry[String, String, Refined[String, Uri]] = ConfigEntry(key, Property, Right(google.com))
      * }}}
      */
    def mapRefineValue[P]: MapRefineValuePartiallyApplied[K, S, V, P] =
      new MapRefineValuePartiallyApplied[K, S, V, P](entry)
  }

  final class MapRefineValuePartiallyApplied[K, S, V, P] private[ciris] (
    val entry: ConfigEntry[K, S, V]
  ) extends AnyVal {

    def apply[A](f: V => A)(
      implicit validate: Validate[A, P],
      typeTag: WeakTypeTag[A Refined P]
    ): ConfigEntry[K, S, A Refined P] = {
      entry.mapValue(f).refineValue[P]
    }
  }
}
