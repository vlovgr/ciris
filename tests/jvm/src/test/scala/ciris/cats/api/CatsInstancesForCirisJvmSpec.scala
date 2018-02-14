package ciris.cats.api

import ciris.PropertySpec
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{ExecutionContext, Future}

final class CatsInstancesForCirisJvmSpec extends PropertySpec with ScalaFutures {
  "CatsInstancesForCirisJvm" when {
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
        ae.raiseError(new Error("message")).failed.futureValue.getCause.getMessage shouldBe "message"
        ae.handleErrorWith(Future.failed(new Error("message")))(e => Future.successful("ok")).futureValue shouldBe "ok"
        ae.pure("ok").futureValue shouldBe "ok"
        ae.product(Future.successful("a"), Future.successful("b")).futureValue shouldBe (("a", "b"))
        ae.map(Future.successful(" a "))(_.trim).futureValue shouldBe "a"

        Apply[Future]
        FlatMap[Future]
        FunctionK[Id, Future]
        Functor[Future]
        Monad[Future]

        val me = MonadError[Future, Throwable]
        me.raiseError(new Error("message")).failed.futureValue.getCause.getMessage shouldBe "message"
        me.handleErrorWith(Future.failed(new Error("message")))(e => Future.successful("ok")).futureValue shouldBe "ok"
        me.pure("ok").futureValue shouldBe "ok"
        me.flatMap(Future.successful("a"))(_ => Future.successful("b")).futureValue shouldBe "b"
        me.product(Future.successful("a"), Future.successful("b")).futureValue shouldBe (("a", "b"))
        me.map(Future.successful(" a "))(_.trim).futureValue shouldBe "a"
      }
    }
  }
}
