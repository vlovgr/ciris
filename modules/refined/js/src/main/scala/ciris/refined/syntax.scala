package ciris.refined

import ciris.{ConfigEntry, ConfigError}
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV

import scala.reflect.ClassTag

object syntax {
  implicit def refinedConfigEntrySyntax[K, S, V](
    entry: ConfigEntry[K, S, V]
  ): RefinedConfigEntrySyntax[K, S, V] = {
    new RefinedConfigEntrySyntax[K, S, V](entry)
  }

  final class RefinedConfigEntrySyntax[K, S, V] private[ciris] (val entry: ConfigEntry[K, S, V])
      extends AnyVal {

    def refineValue[P](
      implicit validate: Validate[V, P],
      classTag: ClassTag[V Refined P]
    ): ConfigEntry[K, S, V Refined P] =
      entry.flatMapValue { value =>
        refineV[P](value).fold(
          error => {
            val typeName = classTag.runtimeClass.getSimpleName
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

    def mapRefineValue[P]: MapRefineValuePartiallyApplied[K, S, V, P] =
      new MapRefineValuePartiallyApplied[K, S, V, P](entry)
  }

  final class MapRefineValuePartiallyApplied[K, S, V, P] private[ciris] (
    val entry: ConfigEntry[K, S, V]
  ) extends AnyVal {

    def apply[A](f: V => A)(
      implicit validate: Validate[A, P],
      classTag: ClassTag[A Refined P]
    ): ConfigEntry[K, S, A Refined P] = {
      entry.mapValue(f).refineValue[P]
    }
  }
}
