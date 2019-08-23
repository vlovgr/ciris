package ciris.squants.generators

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import squants.{Dimension, Quantity}

trait SquantsGenerators {
  def genQuantity[A <: Quantity[A]](dimension: Dimension[A]): Gen[A] =
    for {
      value <- arbitrary[Double]
      unit <- Gen.oneOf(dimension.units.toList)
    } yield unit(value)
}
