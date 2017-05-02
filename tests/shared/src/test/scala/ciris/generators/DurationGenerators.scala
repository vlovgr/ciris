package ciris.generators

import org.scalacheck.{Arbitrary, Gen}

import scala.concurrent.duration.{Duration, FiniteDuration}

trait DurationGenerators {
  val genFiniteDuration: Gen[FiniteDuration] =
    Gen.chooseNum(0, Long.MaxValue).map(Duration.fromNanos)

  implicit val arbFiniteDuration: Arbitrary[FiniteDuration] =
    Arbitrary(genFiniteDuration)

  val genDuration: Gen[Duration] =
    Gen.frequency(
      1 -> Gen.const(Duration.Inf),
      1 -> Gen.const(Duration.MinusInf),
      1 -> Gen.const(Duration.Zero),
      7 -> genFiniteDuration
    )

  implicit val arbDuration: Arbitrary[Duration] =
    Arbitrary(genDuration)
}
