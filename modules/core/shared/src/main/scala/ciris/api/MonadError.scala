package ciris.api

trait MonadError[F[_], E] extends ApplicativeError[F, E] with Monad[F]

object MonadError {
  def apply[F[_], E](implicit me: MonadError[F, E]): MonadError[F, E] = me
}
