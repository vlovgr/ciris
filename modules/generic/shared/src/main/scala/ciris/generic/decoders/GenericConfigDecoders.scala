package ciris.generic.decoders

import ciris.{ConfigError, ConfigDecoder, ConfigEntry}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HNil, Inl, Inr, Lazy}

trait GenericConfigDecoders {
  implicit val cNilConfigDecoder: ConfigDecoder[CNil] =
    new ConfigDecoder[CNil] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, CNil] =
        Left(ConfigError(
          s"Could not find any valid coproduct choice while decoding ${entry.keyType.name} [${entry.key}]"))
    }

  implicit def coproductConfigDecoder[A, B <: Coproduct](
    implicit decodeA: Lazy[ConfigDecoder[A]],
    decodeB: ConfigDecoder[B]
  ): ConfigDecoder[A :+: B] =
    new ConfigDecoder[A :+: B] {
      override def decode[Key, S](
        entry: ConfigEntry[Key, S, String]): Either[ConfigError, A :+: B] =
        decodeA.value.decode(entry) match {
          case Right(a) => Right(Inl(a))
          case Left(aError) =>
            decodeB.decode(entry) match {
              case Right(b)     => Right(Inr(b))
              case Left(bError) => Left(aError combine bError)
            }
        }
    }

  implicit def unaryHListConfigDecoder[A](
    implicit decodeA: Lazy[ConfigDecoder[A]]
  ): ConfigDecoder[A :: HNil] = {
    decodeA.value.map(_ :: HNil)
  }

  implicit def genericConfigDecoder[A, B](
    implicit gen: Generic.Aux[A, B],
    decodeB: Lazy[ConfigDecoder[B]]
  ): ConfigDecoder[A] = {
    decodeB.value.map(gen.from)
  }
}
