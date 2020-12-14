package ciris.refined

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import ciris._
import eu.timepit.refined.types.numeric.PosInt
import org.scalatest.funsuite.AnyFunSuite

final class RefinedSpec extends AnyFunSuite {
  test("refTypeConfigDecoder.success") {
    assert {
      val actual = default("1").as[PosInt].attempt[IO].unsafeRunSync().map(_.value)
      val expected = Right(1)
      actual == expected
    }
  }

  test("refTypeConfigDecoder.error") {
    assert {
      default("0").as[PosInt].attempt[IO].unsafeRunSync().isLeft
    }
  }
}
