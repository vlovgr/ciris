package ciris.decoders

import ciris.{ConfigDecoder, Secret}

trait CirisConfigDecoders {
  implicit def secretConfigDecoder[A](
    implicit decoder: ConfigDecoder[A]
  ): ConfigDecoder[Secret[A]] = {
    decoder.map(Secret.apply)
  }
}
