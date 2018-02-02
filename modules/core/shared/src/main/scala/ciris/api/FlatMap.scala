package ciris.api

trait FlatMap[F[_]] extends Apply[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

object FlatMap {
  def apply[F[_]](implicit f: FlatMap[F]): FlatMap[F] = f
}
