package ciris.generic.readers

import ciris.{ConfigError, ConfigReader, ConfigEntry}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HNil, Inl, Inr, Lazy}

trait GenericConfigReaders {
  implicit val cNilConfigReader: ConfigReader[CNil] =
    new ConfigReader[CNil] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, CNil] =
        Left(ConfigError(s"Could not find any valid coproduct choice while reading ${entry.keyType.name} [${entry.key}]"))
    }

  implicit def coproductConfigReader[A, B <: Coproduct](
    implicit readA: Lazy[ConfigReader[A]],
    readB: ConfigReader[B]
  ): ConfigReader[A :+: B] =
    new ConfigReader[A :+: B] {
      override def read[Key, S](entry: ConfigEntry[Key, S, String]): Either[ConfigError, A :+: B] =
        readA.value.read(entry) match {
          case Right(a) => Right(Inl(a))
          case Left(aError) =>
            readB.read(entry) match {
              case Right(b) => Right(Inr(b))
              case Left(bError) => Left(aError combine bError)
            }
        }
    }

  implicit def unaryHListConfigReader[A](
    implicit readA: Lazy[ConfigReader[A]]
  ): ConfigReader[A :: HNil] = {
    readA.value.map(_ :: HNil)
  }

  implicit def genericConfigReader[A, B](
    implicit gen: Generic.Aux[A, B],
    readB: Lazy[ConfigReader[B]]
  ): ConfigReader[A] = {
    readB.value.map(gen.from)
  }
}
