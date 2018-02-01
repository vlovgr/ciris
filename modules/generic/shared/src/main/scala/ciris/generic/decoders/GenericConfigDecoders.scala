package ciris.generic.decoders

import ciris.{ConfigError, ConfigDecoder, ConfigEntry}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

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

  implicit def hNilConfigDecoder[A]: ConfigDecoder[A, HNil] =
    new ConfigDecoder[A, HNil] {
      override def decode[K, S](entry: ConfigEntry[K, S, A]): Either[ConfigError, HNil] =
        Right(HNil)
    }

  implicit def hListConfigDecoder[A, B, C <: HList](
    implicit decodeB: Lazy[ConfigDecoder[A, B]],
    decodeC: ConfigDecoder[A, C]
  ): ConfigDecoder[A, B :: C] =
    new ConfigDecoder[A, B :: C] {
      override def decode[K, S](entry: ConfigEntry[K, S, A]): Either[ConfigError, B :: C] = {
        (decodeB.value.decode(entry), decodeC.decode(entry)) match {
          case (Right(b), Right(c)) => Right(b :: c)
          case (Left(error1), Right(_)) => Left(error1)
          case (Right(_), Left(error2)) => Left(error2)
          case (Left(error1), Left(error2)) => Left(error1 combine error2)
        }
      }
    }

  implicit def genericConfigDecoder[A, B, C](
    implicit gen: Generic.Aux[C, B],
    decodeB: Lazy[ConfigDecoder[A, B]]
  ): ConfigDecoder[A, C] = {
    decodeB.value.map(gen.from)
  }
}
