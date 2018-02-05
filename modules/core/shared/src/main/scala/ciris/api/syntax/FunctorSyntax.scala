package ciris.api.syntax

import ciris.api.Functor

trait FunctorSyntax {
  implicit def functorSyntax[F[_]: Functor, A](fa: F[A]): FunctorSyntaxOps[F, A] =
    new FunctorSyntaxOps[F, A](fa)

  final class FunctorSyntaxOps[F[_]: Functor, A](val fa: F[A]) {
    def map[B](f: A => B): F[B] = Functor[F].map(fa)(f)
  }
}
