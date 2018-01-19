package ciris.readers

import ciris.{ConfigError, ConfigReader, ConfigEntry}

trait DerivedConfigReaders {
  implicit def optionConfigReader[Value: ConfigReader]: ConfigReader[Option[Value]] =
    new ConfigReader[Option[Value]] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, Option[Value]] =
        entry.value.fold(_ => Right(None), _ => ConfigReader[Value].read(entry).right.map(Some.apply))
    }
}
