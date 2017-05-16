package ciris.readers

import ciris.ConfigReader
import ciris.ConfigReader.fold

trait DerivedConfigReaders {
  implicit def optionConfigReader[T](implicit reader: ConfigReader[T]): ConfigReader[Option[T]] =
    fold(_ => Right(None), (key, _, source) => reader.read(key)(source).right.map(Some(_)))
}
