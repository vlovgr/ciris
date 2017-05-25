package ciris.readers

import ciris.PropertySpec
import ciris.generators.DurationGenerators
import org.scalacheck.Gen

import scala.concurrent.duration.{Duration, FiniteDuration}

final class DurationConfigReadersSpec extends PropertySpec with DurationGenerators {
  "DurationConfigReaders" when {
    "reading a Duration" should {
      "successfully read Duration values" in {
        forAll { duration: Duration =>
          val durationString =
            if (duration.toString startsWith "Duration.")
              duration.toString.drop(9)
            else duration.toString

          val value = readValue[Duration](durationString)
          if (duration.isFinite)
            value.right.map(_.toNanos) shouldBe Right(duration.toNanos)
          else
            value shouldBe Right(duration)
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(Duration(string))) {
            readValue[Duration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a FiniteDuration" should {
      "successfully read FiniteDuration values" in {
        forAll { finiteDuration: FiniteDuration =>
          val value = readValue[FiniteDuration](finiteDuration.toString)
          value.right.map(_.toNanos) shouldBe Right(finiteDuration.toNanos)
        }
      }

      "return a failure for infinite durations" in {
        val infiniteDurations = List("Inf", "PlusInf", "+Inf", "MinusInf", "-Inf")
        forAll(Gen.oneOf(infiniteDurations)){ infiniteDuration =>
          readValue[FiniteDuration](infiniteDuration) shouldBe a[Left[_, _]]
        }
      }

      "return a failure for other values" in {
        forAll { string: String =>
          whenever(fails(Duration(string)) || !Duration(string).isFinite()) {
            readValue[FiniteDuration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
