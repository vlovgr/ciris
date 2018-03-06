package ciris.cats.effect

import cats.effect.IO
import ciris.cats.effect.syntax._
import ciris.{ConfigKeyType, ConfigSource, PropertySpec}

final class CatsEffectSyntaxSpec extends PropertySpec {
  "CatsEffectSyntax" when {
    "using suspendMemoizeF" should {
      "suspend and memoize reading" in {
        var timesRead: Int = 0

        val source: ConfigSource[IO, String, Unit] =
          ConfigSource(ConfigKeyType[String]("key")) { _ =>
            timesRead = timesRead + 1
            Right(())
          }.suspendMemoizeF[IO]

        val entry = source.read("key")

        timesRead shouldBe 0

        entry.value.unsafeRunSync()

        timesRead shouldBe 1

        entry.value.unsafeRunSync()

        timesRead shouldBe 1

        entry.sourceValue.unsafeRunSync()

        timesRead shouldBe 1
      }
    }
  }
}
