package ciris.cats.api

import cats.arrow
import ciris.PropertySpec

import scala.concurrent.{ExecutionContext, Future}
import scala.collection.immutable.Queue
import scala.util.Try

final class CatsInstancesForCirisSpec extends PropertySpec {
  "CatsInstancesForCiris" when {
    "providing instances for Future" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        implicit val executionContext: ExecutionContext =
          ExecutionContext.Implicits.global

        Applicative[Future]
        Apply[Future]
        FlatMap[Future]
        FunctionK[Id, Future]
        Functor[Future]
        Monad[Future]
      }
    }

    "providing instances for List" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        val applicative = catsApplicativeToCiris[List]
        applicative.pure(123) shouldBe List(123)
        applicative.product(List(123), List("456")) shouldBe List((123, "456"))
        applicative.map(List(123))(_ + 1) shouldBe List(124)

        val apply = catsApplyToCiris[List]
        apply.product(List(123), List("456")) shouldBe List((123, "456"))
        apply.map(List(123))(_ + 1) shouldBe List(124)

        val flatMap = catsFlatMapToCiris[List]
        flatMap.flatMap(List.empty[Int])(_ => List.empty) shouldBe Nil
        flatMap.product(List(123), List("456")) shouldBe List((123, "456"))
        flatMap.map(List(123))(_ + 1) shouldBe List(124)

        implicit val idToList: _root_.cats.~>[Id, List] =
          new arrow.FunctionK[Id, List] {
            override def apply[A](fa: Id[A]): List[A] =
              Applicative[List].pure(fa)
          }

        val functionK = catsFunctionKToCiris[Id, List]
        functionK(123) shouldBe List(123)

        val functor = catsFunctorToCiris[List]
        functor.map(List(123))(_ + 1) shouldBe List(124)

        val monad = catsMonadToCiris[List]
        monad.flatMap(List.empty[Int])(_ => List.empty) shouldBe Nil
        monad.pure(123) shouldBe List(123)
        monad.product(List(123), List("456")) shouldBe List((123, "456"))
        monad.map(List(123))(_ + 1) shouldBe List(124)
      }
    }

    "providing instances for Queue" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        Applicative[Queue]
        Apply[Queue]
        FlatMap[Queue]
        FunctionK[Id, Queue]
        Functor[Queue]
        Monad[Queue]
      }
    }

    "providing instances for Stream" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        Applicative[Stream]
        Apply[Stream]
        FlatMap[Stream]
        FunctionK[Id, Stream]
        Functor[Stream]
        Monad[Stream]
      }
    }

    "providing instances for Try" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        Applicative[Try]
        Apply[Try]
        FlatMap[Try]
        FunctionK[Id, Try]
        Functor[Try]
        Monad[Try]
      }
    }

    "providing instances for Vector" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        Applicative[Vector]
        Apply[Vector]
        FlatMap[Vector]
        FunctionK[Id, Vector]
        Functor[Vector]
        Monad[Vector]
      }
    }
  }
}
