package ciris.refined

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import ciris._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.PosInt
import org.scalatest.funsuite.AnyFunSuite

final class RefinedSpec extends AnyFunSuite {
  test("refTypeConfigDecoder.success") {
    assert {
      val actual = default("1").as[PosInt].attempt[IO].unsafeRunSync()
      val expected = Right(1: PosInt)
      actual == expected
    }
  }

  test("refTypeConfigDecoder.error") {
    assert {
      val actual = default("0").as[PosInt].attempt[IO].unsafeRunSync()

      val expected =
        Left {
          ConfigError.sensitive(
            "Unable to convert value 0 to eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]",
            "Unable to convert value to eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]"
          )
        }

      actual == expected
    }
  }
}
