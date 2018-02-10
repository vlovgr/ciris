package ciris.api.syntax

import ciris.api.FlatMap

trait FlatMapSyntax {
  implicit def flatMapSyntaxOps[F[_]: FlatMap, A](fa: F[A]): FlatMapSyntaxOps[F, A] =
    new FlatMapSyntaxOps(fa)

  final class FlatMapSyntaxOps[F[_]: FlatMap, A](fa: F[A]) {
    def flatMap[B](f: A => F[B]): F[B] = FlatMap[F].flatMap(fa)(f)
  }
}
