package ciris.api

trait Applicative[F[_]] extends Apply[F] {
  def pure[A](x: A): F[A]
}

object Applicative {
  def apply[F[_]](implicit a: Applicative[F]): Applicative[F] = a
}
