package ciris.api

trait ApplicativeError[F[_], E] extends Applicative[F] {
  def raiseError[A](e: E): F[A]
  def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]
}

object ApplicativeError {
  def apply[F[_], E](implicit ae: ApplicativeError[F, E]): ApplicativeError[F, E] = ae
}
