package ciris.readers

import ciris.PropertySpec
import ciris.generators.DurationGenerators

import scala.concurrent.duration.{Duration, FiniteDuration}

final class DurationConfigReadersSpec extends PropertySpec with DurationGenerators {
  "DurationConfigReaders" when {
    "reading a Duration" should {
      "successfully read Duration values" in {
        forAll { duration: Duration ⇒
          val durationString =
            if (duration.toString startsWith "Duration.")
              duration.toString.drop(9)
            else duration.toString

          readValue[Duration](durationString) shouldBe Right(duration)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Duration(string))) {
            readValue[Duration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }

    "reading a FiniteDuration" should {
      "successfully read FiniteDuration values" in {
        forAll { finiteDuration: FiniteDuration ⇒
          readValue[FiniteDuration](finiteDuration.toString) shouldBe Right(finiteDuration)
        }
      }

      "return a failure for other values" in {
        forAll { string: String ⇒
          whenever(fails(Duration(string)) || !Duration(string).isFinite()) {
            readValue[FiniteDuration](string) shouldBe a[Left[_, _]]
          }
        }
      }
    }
  }
}
