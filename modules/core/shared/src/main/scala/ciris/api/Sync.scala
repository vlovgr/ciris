package ciris.api

trait Sync[F[_]] extends MonadError[F, Throwable] {
  def suspend[A](thunk: => F[A]): F[A]
}

object Sync {
  def apply[F[_]](implicit sync: Sync[F]): Sync[F] = sync
}
