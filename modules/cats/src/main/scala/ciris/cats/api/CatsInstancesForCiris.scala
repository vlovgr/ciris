package ciris.cats.api

trait CatsInstancesForCiris extends CatsInstancesForCiris1 {
  implicit def catsMonadErrorToCiris[F[_], E](
    implicit me: _root_.cats.MonadError[F, E]
  ): _root_.ciris.api.MonadError[F, E] = new _root_.ciris.api.MonadError[F, E] {
    override def raiseError[A](e: E): F[A] = me.raiseError(e)
    override def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A] = me.handleErrorWith(fa)(f)
    override def pure[A](x: A): F[A] = me.pure(x)
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = me.flatMap(fa)(f)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = me.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = me.map(fa)(f)
  }

  implicit def catsFunctionKToCiris[F[_], G[_]](
    implicit fk: _root_.cats.~>[F, G]
  ): _root_.ciris.api.FunctionK[F, G] = new _root_.ciris.api.FunctionK[F, G] {
    override def apply[A](fa: F[A]): G[A] = fk.apply(fa)
  }
}

private[ciris] trait CatsInstancesForCiris1 extends CatsInstancesForCiris2 {
  implicit def catsApplicativeErrorToCiris[F[_], E](
    implicit ae: _root_.cats.ApplicativeError[F, E]
  ): _root_.ciris.api.ApplicativeError[F, E] = new _root_.ciris.api.ApplicativeError[F, E] {
    override def raiseError[A](e: E): F[A] = ae.raiseError(e)
    override def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A] = ae.handleErrorWith(fa)(f)
    override def pure[A](x: A): F[A] = ae.pure(x)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ae.product(fa, fb)
    override def map[A, B](fa: F[A])(f: Function[A, B]): F[B] = ae.map(fa)(f)
  }
}

private[ciris] trait CatsInstancesForCiris2 extends CatsInstancesForCiris3 {
  implicit def catsMonadToCiris[F[_]](
    implicit m: _root_.cats.Monad[F]
  ): _root_.ciris.api.Monad[F] = new _root_.ciris.api.Monad[F] {
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = m.flatMap(fa)(f)
    override def pure[A](x: A): F[A] = m.pure(x)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = m.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = m.map(fa)(f)
  }
}

private[ciris] trait CatsInstancesForCiris3 extends CatsInstancesForCiris4 {
  implicit def catsFlatMapToCiris[F[_]](
    implicit fm: _root_.cats.FlatMap[F]
  ): _root_.ciris.api.FlatMap[F] = new _root_.ciris.api.FlatMap[F] {
    override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = fm.flatMap(fa)(f)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = fm.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = fm.map(fa)(f)
  }

  implicit def catsApplicativeToCiris[F[_]](
    implicit a: _root_.cats.Applicative[F]
  ): _root_.ciris.api.Applicative[F] = new _root_.ciris.api.Applicative[F] {
    override def pure[A](x: A): F[A] = a.pure(x)
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = a.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = a.map(fa)(f)
  }
}

private[ciris] trait CatsInstancesForCiris4 extends CatsInstancesForCiris5 {
  implicit def catsApplyToCiris[F[_]](
    implicit a: _root_.cats.Apply[F]
  ): _root_.ciris.api.Apply[F] = new _root_.ciris.api.Apply[F] {
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = a.product(fa, fb)
    override def map[A, B](fa: F[A])(f: A => B): F[B] = a.map(fa)(f)
  }
}

private[ciris] trait CatsInstancesForCiris5 {
  implicit def catsFunctorToCiris[F[_]](
    implicit fu: _root_.cats.Functor[F]
  ): _root_.ciris.api.Functor[F] = new _root_.ciris.api.Functor[F] {
    override def map[A, B](fa: F[A])(f: A => B): F[B] = fu.map(fa)(f)
  }
}
