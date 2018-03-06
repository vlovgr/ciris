package ciris.cats.effect

import cats.Eval
import cats.effect.implicits._
import cats.effect.{IO, LiftIO}
import ciris.ConfigSource
import ciris.api.{Apply, Id}

object syntax {
  implicit def catsEffectConfigSourceIdSyntax[K, V](
    source: ConfigSource[Id, K, V]
  ): CatsEffectConfigSourceIdSyntax[K, V] = {
    new CatsEffectConfigSourceIdSyntax[K, V](source)
  }

  final class CatsEffectConfigSourceIdSyntax[K, V] private[ciris] (
    val source: ConfigSource[Id, K, V]
  ) extends AnyVal {

    /**
      * Suspends the reading of this configuration source into
      * context `F`, while also memoizing values, so they are
      * not being read more than once for the same reading.
      *
      * @tparam F the context in which to suspend and memoize reading
      * @return a new [[ConfigSource]]
      */
    def suspendMemoizeF[F[_]: Apply: LiftIO]: ConfigSource[F, K, V] =
      ConfigSource.applyF(source.keyType) { key =>
        IO.eval(Eval.later(source.read(key).value)).liftIO[F]
      }
  }
}
