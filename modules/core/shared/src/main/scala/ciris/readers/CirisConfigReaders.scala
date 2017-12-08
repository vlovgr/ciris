package ciris.readers

import ciris.{ConfigReader, Secret}

trait CirisConfigReaders {
  implicit def secretConfigReader[A](implicit reader: ConfigReader[A]): ConfigReader[Secret[A]] =
    reader.map(Secret.apply)
}
