package ciris.generic.decoders

import ciris.{ConfigError, ConfigDecoder, ConfigEntry}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HNil, Inl, Inr, Lazy}

trait GenericConfigDecoders {
  implicit def cNilConfigDecoder[A]: ConfigDecoder[A, CNil] =
    new ConfigDecoder[A, CNil] {
      override def decode[K, S](entry: ConfigEntry[K, S, A]): Either[ConfigError, CNil] =
        Left(ConfigError {
          s"Could not find any valid coproduct choice while decoding ${entry.keyType.name} [${entry.key}]"
        })
    }

  implicit def coproductConfigDecoder[A, B <: Coproduct, C](
    implicit decodeC: Lazy[ConfigDecoder[A, C]],
    decodeB: ConfigDecoder[A, B]
  ): ConfigDecoder[A, C :+: B] =
    new ConfigDecoder[A, C :+: B] {
      override def decode[K, S](
        entry: ConfigEntry[K, S, A]
      ): Either[ConfigError, C :+: B] =
        decodeC.value.decode(entry) match {
          case Right(a) => Right(Inl(a))
          case Left(aError) =>
            decodeB.decode(entry) match {
              case Right(b)     => Right(Inr(b))
              case Left(bError) => Left(aError combine bError)
            }
        }
    }

  implicit def unaryHListConfigDecoder[A, B](
    implicit decodeB: Lazy[ConfigDecoder[A, B]]
  ): ConfigDecoder[A, B :: HNil] = {
    decodeB.value.map(_ :: HNil)
  }

  implicit def genericConfigDecoder[A, B, C](
    implicit gen: Generic.Aux[C, B],
    decodeB: Lazy[ConfigDecoder[A, B]]
  ): ConfigDecoder[A, C] = {
    decodeB.value.map(gen.from)
  }
}
