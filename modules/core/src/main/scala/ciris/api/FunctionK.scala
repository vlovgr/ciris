package ciris.api

trait FunctionK[F[_], G[_]] {
  def apply[A](fa: F[A]): G[A]
}

object FunctionK {
  def apply[F[_], G[_]](implicit fk: FunctionK[F, G]): FunctionK[F, G] = fk
}
