package ciris.refined

import ciris.api._
import ciris.api.syntax._
import ciris.{ConfigEntry, ConfigError}
import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV

import scala.reflect.ClassTag

object syntax {
  implicit def refinedConfigEntrySyntax[F[_]: Monad, K, S, V](
    entry: ConfigEntry[F, K, S, V]
  ): RefinedConfigEntrySyntax[F, K, S, V] = {
    new RefinedConfigEntrySyntax(entry)
  }

  final class RefinedConfigEntrySyntax[F[_]: Monad, K, S, V] private[ciris] (
    val entry: ConfigEntry[F, K, S, V]
  ) {
    def refineValue[P](
      implicit validate: Validate[V, P],
      classTag: ClassTag[V Refined P]
    ): ConfigEntry[F, K, S, V Refined P] =
      entry.withValueF {
        for {
          sourceValue <- entry.sourceValue
          errorOrValue <- entry.value
        } yield {
          errorOrValue.right.flatMap { value =>
            refineV[P](value).fold(
              error => {
                val typeName = classTag.runtimeClass.getSimpleName
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

    def mapRefineValue[P]: MapRefineValuePartiallyApplied[F, K, S, V, P] =
      new MapRefineValuePartiallyApplied(entry)
  }

  final class MapRefineValuePartiallyApplied[F[_]: Monad, K, S, V, P] private[ciris] (
    val entry: ConfigEntry[F, K, S, V]
  ) {
    def apply[A](f: V => A)(
      implicit validate: Validate[A, P],
      classTag: ClassTag[A Refined P]
    ): ConfigEntry[F, K, S, A Refined P] = {
      entry.mapValue(f).refineValue[P]
    }
  }
}
