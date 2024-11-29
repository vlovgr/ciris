package ciris.internal

import cats.effect.kernel.Async

/**
  * Equivalent to `[F[_]] =>> Async[F] ?=> F[A]` in Scala 3.
  */
trait AsyncEvaluation[A] {

  def toEffect[F[_]](implicit F: Async[F]): F[A]
}
