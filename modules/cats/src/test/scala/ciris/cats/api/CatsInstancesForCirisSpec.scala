package ciris.cats.api

import cats.arrow
import ciris.PropertySpec
import org.scalatest.concurrent.ScalaFutures

import scala.collection.immutable.Queue
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

final class CatsInstancesForCirisSpec extends PropertySpec with ScalaFutures {
  "CatsInstancesForCiris" when {
    "providing instances for Future" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        implicit val executionContext: ExecutionContext =
          ExecutionContext.Implicits.global

        Applicative[Future]
        ApplicativeError[Future, Throwable]
        val ae = catsApplicativeErrorToCiris[Future, Throwable]
        ae.raiseError(new Error("message"))
          .failed
          .futureValue
          .getCause
          .getMessage shouldBe "message"
        ae.handleErrorWith(Future.failed(new Error("message")))(e => Future.successful("ok"))
          .futureValue shouldBe "ok"
        ae.pure("ok").futureValue shouldBe "ok"
        ae.product(Future.successful("a"), Future.successful("b")).futureValue shouldBe (("a", "b"))
        ae.map(Future.successful(" a "))(_.trim).futureValue shouldBe "a"

        Apply[Future]
        FlatMap[Future]
        FunctionK[Id, Future]
        Functor[Future]
        Monad[Future]

        val me = MonadError[Future, Throwable]
        me.raiseError(new Error("message"))
          .failed
          .futureValue
          .getCause
          .getMessage shouldBe "message"
        me.handleErrorWith(Future.failed(new Error("message")))(e => Future.successful("ok"))
          .futureValue shouldBe "ok"
        me.pure("ok").futureValue shouldBe "ok"
        me.flatMap(Future.successful("a"))(_ => Future.successful("b")).futureValue shouldBe "b"
        me.product(Future.successful("a"), Future.successful("b")).futureValue shouldBe (("a", "b"))
        me.map(Future.successful(" a "))(_.trim).futureValue shouldBe "a"
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

    "providing instances for Try" should {
      "be able to provide all required instances" in {
        import _root_.cats.implicits._
        import ciris.api._
        import ciris.cats._

        Applicative[Try]
        ApplicativeError[Try, Throwable]
        catsApplicativeErrorToCiris[Try, Throwable]
        Apply[Try]
        FlatMap[Try]
        FunctionK[Id, Try]
        Functor[Try]
        Monad[Try]
        MonadError[Try, Throwable]
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
