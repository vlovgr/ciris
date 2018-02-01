package ciris.decoders

import ciris.{ConfigDecoder, Secret}

private[ciris] trait CirisConfigDecoders {
  implicit def secretConfigDecoder[A, B](
    implicit decoder: ConfigDecoder[A, B]
  ): ConfigDecoder[A, Secret[B]] = {
    decoder.map(Secret.apply)
  }
}
