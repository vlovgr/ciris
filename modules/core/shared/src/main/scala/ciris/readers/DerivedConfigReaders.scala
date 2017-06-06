package ciris.readers

import ciris.{ConfigError, ConfigReader, ConfigSourceEntry}

trait DerivedConfigReaders {
  implicit def optionConfigReader[Value: ConfigReader]: ConfigReader[Option[Value]] =
    new ConfigReader[Option[Value]] {
      override def read[Key](entry: ConfigSourceEntry[Key]): Either[ConfigError, Option[Value]] =
        entry.value.fold(_ => Right(None), _ => ConfigReader[Value].read(entry).right.map(Some.apply))
    }
}
