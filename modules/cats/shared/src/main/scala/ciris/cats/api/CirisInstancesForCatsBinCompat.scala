package ciris.cats.api

import cats.Semigroup
import ciris._

trait CirisInstancesForCatsBinCompat {
  implicit val semigroupConfigErrors: Semigroup[ConfigErrors] = new Semigroup[ConfigErrors] {
    def combine(first: ConfigErrors, second: ConfigErrors) =
      ConfigErrors.combine(first, second)
  }
}
