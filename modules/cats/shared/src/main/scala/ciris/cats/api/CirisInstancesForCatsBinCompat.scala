package ciris.cats.api

import cats.Semigroup
import ciris._

trait CirisInstancesForCatsBinCompat {

  implicit val semigroupConfigErrors: Semigroup[ConfigErrors] =
    Semigroup.instance(_ combine _)
}
