package ciris.api

trait Monad[F[_]] extends FlatMap[F] with Applicative[F]

object Monad {
  def apply[F[_]](implicit m: Monad[F]): Monad[F] = m
}
