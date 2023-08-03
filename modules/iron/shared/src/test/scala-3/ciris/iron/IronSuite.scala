package ciris.iron

import cats.effect.IO
import cats.syntax.all.*
import ciris.*
import ciris.iron.given
import io.github.iltotore.iron.*
import munit.FunSuite
import cats.effect.unsafe.implicits.global
import io.github.iltotore.iron.constraint.numeric.Positive

final class IronSuite extends FunSuite {

  test("ironConfigDecoder.success") {
    assert(
      default("1")
        .as[Int :| Positive]
        .attempt[IO]
        .map(_.map(i => i == 1))
        .unsafeRunSync()
        .isRight
    )
  }

  test("ironConfigDecoder.error") {
    assert(
      default("-1")
        .as[Int :| Positive]
        .attempt[IO]
        .map(_.map(i => i == 1))
        .unsafeRunSync()
        .isLeft
    )
  }
}
