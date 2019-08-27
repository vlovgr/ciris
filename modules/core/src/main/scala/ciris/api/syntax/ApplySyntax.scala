package ciris.api.syntax

import ciris.api.Apply

trait ApplySyntax {
  implicit def applySyntaxOps[F[_]: Apply, A](fa: F[A]): ApplySyntaxOps[F, A] =
    new ApplySyntaxOps(fa)

  final class ApplySyntaxOps[F[_]: Apply, A](val fa: F[A]) {
    def product[B](fb: F[B]): F[(A, B)] = Apply[F].product(fa, fb)
  }
}
