package ciris.cats.effect.api

trait CatsEffectInstancesForCiris {
  implicit def catsEffectSyncToCiris[F[_]](
    implicit s: _root_.cats.effect.Sync[F]
  ): _root_.ciris.api.Sync[F] = new _root_.ciris.api.Sync[F] {
    override def suspend[A](thunk: => F[A]): F[A] = s.suspend(thunk)
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = s.flatMap(fa)(f)
    override def raiseError[A](e: Throwable): F[A] = s.raiseError(e)
    override def handleErrorWith[A](fa: F[A])(f: Throwable => F[A]): F[A] = s.handleErrorWith(fa)(f)
    override def pure[A](x: A): F[A] = s.pure(x)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = s.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = s.map(fa)(f)
  }
}
