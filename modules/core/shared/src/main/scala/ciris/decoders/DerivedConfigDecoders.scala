package ciris.decoders

import ciris.{ConfigError, ConfigDecoder, ConfigEntry}

private[ciris] trait DerivedConfigDecoders {
  implicit def optionConfigDecoder[A, B](
    implicit decoder: ConfigDecoder[A, B]
  ): ConfigDecoder[A, Option[B]] =
    new ConfigDecoder[A, Option[B]] {
      override def decode[K, S](entry: ConfigEntry[K, S, A]): Either[ConfigError, Option[B]] =
        entry.value.fold(_ => Right(None), _ => decoder.decode(entry).right.map(Some.apply))
    }
}
