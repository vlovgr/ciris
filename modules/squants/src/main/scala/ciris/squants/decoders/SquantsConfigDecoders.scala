package ciris.squants.decoders

import ciris.ConfigDecoder
import squants.{Dimension, Quantity}

trait SquantsConfigDecoders {
  implicit def dimensionConfigDecoder[A <: Quantity[A]](
    implicit dimension: Dimension[A]
  ): ConfigDecoder[String, A] =
    ConfigDecoder.fromTry(dimension.name)(dimension.parseString)
}
