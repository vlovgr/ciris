package ciris.cats.effect

import cats.effect.{ContextShift, IO}
import ciris.cats.effect.syntax._
import ciris.{ConfigKeyType, ConfigSource, PropertySpec}

import scala.concurrent.ExecutionContext.global

final class CatsEffectSyntaxSpec extends PropertySpec {
  implicit val contextShift: ContextShift[IO] =
    IO.contextShift(global)

  "CatsEffectSyntax" when {
    "using suspendMemoizeF" should {
      "suspend and memoize reading" in {
        var timesRead: Int = 0

        val source =
          ConfigSource(ConfigKeyType[String]("key")) { _ =>
            timesRead = timesRead + 1
            Right(())
          }.suspendMemoizeF[IO]

        (for {
          memoizedValue <- source.read("key").value
          _ <- IO(timesRead shouldBe 0)
          value <- memoizedValue
          _ <- IO(timesRead shouldBe 1)
          _ <- memoizedValue
          _ <- IO(timesRead shouldBe 1)
        } yield ()).unsafeRunSync()
      }
    }
  }
}
