package ciris.api

trait Apply[F[_]] extends Functor[F] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}

object Apply {
  def apply[F[_]](implicit a: Apply[F]): Apply[F] = a
}
