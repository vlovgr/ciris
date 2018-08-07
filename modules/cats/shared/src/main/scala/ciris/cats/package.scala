package ciris

import ciris.cats.api.{CatsInstancesForCiris, CirisInstancesForCats, CirisInstancesForCatsBinCompat}

/**
  * Module providing an integration with [[https://github.com/typelevel/cats cats]].
  */
package object cats extends CatsInstancesForCiris with CirisInstancesForCats with CirisInstancesForCatsBinCompat
