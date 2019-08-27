package ciris

import ciris.cats.api.{CatsInstancesForCiris, CirisInstancesForCats}

/**
  * Module providing an integration with [[https://github.com/typelevel/cats cats]].
  */
package object cats extends CatsInstancesForCiris with CirisInstancesForCats
