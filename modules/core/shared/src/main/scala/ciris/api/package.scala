package ciris

package object api {
  type Id[A] = A

  implicit object Id extends Monad[Id] {
    override def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
    override def product[A, B](fa: Id[A], fb: Id[B]): Id[(A, B)] = (fa, fb)
    override def pure[A](x: A): Id[A] = x
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
  }
}
