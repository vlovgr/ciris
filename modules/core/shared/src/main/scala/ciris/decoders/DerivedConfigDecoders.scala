package ciris.decoders

import ciris.{ConfigError, ConfigDecoder, ConfigEntry}

trait DerivedConfigDecoders {
  implicit def optionConfigDecoder[Value: ConfigDecoder]: ConfigDecoder[Option[Value]] =
    new ConfigDecoder[Option[Value]] {
      override def decode[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, Option[Value]] =
        entry.value.fold(_ => Right(None), _ => ConfigDecoder[Value].decode(entry).right.map(Some.apply))
    }
}
