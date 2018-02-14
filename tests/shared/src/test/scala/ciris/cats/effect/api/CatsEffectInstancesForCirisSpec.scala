package ciris.cats.effect.api

import ciris.PropertySpec
import ciris.api.Sync

final class CatsEffectInstancesForCirisSpec extends PropertySpec {
  "CatsEffectInstancesForCiris" when {
    "providing instances for IO" should {
      "be able to provide all required instances" in {
        import _root_.cats.effect.IO
        import ciris.cats.effect._

        val s = Sync[IO]
        s.suspend(IO(1)).unsafeRunSync() shouldBe 1
        s.flatMap(IO(1))(_ => IO(2)).unsafeRunSync() shouldBe 2
        s.raiseError(new Error("message")).attempt.unsafeRunSync() shouldBe a[Left[_, _]]
        s.handleErrorWith(IO.raiseError(new Error("message")))(_ => IO(1)).unsafeRunSync() shouldBe 1
        s.pure(1).unsafeRunSync() shouldBe 1
        s.product(IO(1), IO(2)).unsafeRunSync() shouldBe ((1, 2))
        s.map(IO(1))(a => 2 + a).unsafeRunSync() shouldBe 3
      }

      "be able to use it together with ciris-cats" in {
        import ciris._
        import ciris.cats._
        import ciris.cats.effect._
        import _root_.cats.effect.IO

        case class TestConfig(env: String, prop: String)

        val config =
          loadConfig(
            envF[IO, String]("TEST"),
            propF[IO, String]("file.encoding")
          )(TestConfig)
      }
    }
  }
}
