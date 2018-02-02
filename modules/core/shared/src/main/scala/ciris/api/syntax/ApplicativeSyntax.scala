package ciris.api.syntax

import ciris.api.Applicative

trait ApplicativeSyntax {
  implicit def applicativeSyntax[A](a: A): ApplicativeSyntaxOps[A] =
    new ApplicativeSyntaxOps(a)

  final class ApplicativeSyntaxOps[A](a: A) {
    def pure[F[_]: Applicative]: F[A] = Applicative[F].pure(a)
  }
}
