package ciris

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._

final class UseOnceSecretSpec extends BaseSpec {
  test("UseOnceSecret.nullifies") {
    forAll { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .flatMap(_.useOnce(_ => IO.unit))
        .flatMap(_ => IO(assert(value.forall(_ == ' '))))
        .unsafeRunSync()
    }
  }

  test("UseOnceSecret.useOnce") {
    forAll { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .flatMap(_.useOnce(s => IO(assert(s eq value))))
        .unsafeRunSync()
    }
  }

  test("UseOnceSecret.useTwice") {
    forAll { (value: Array[Char]) =>
      UseOnceSecret[IO](value)
        .map(_.useOnce(_ => IO.unit))
        .flatMap(use => (use, use).parTupled)
        .attemptNarrow[IllegalStateException]
        .flatMap(e => IO(assert(e.isLeft)))
        .unsafeRunSync()
    }
  }
}
