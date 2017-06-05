package ciris.generic.readers

import ciris.{ConfigError, ConfigReader}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HNil, Inl, Inr, Lazy}

trait GenericConfigReaders {
  implicit val cNilConfigReader: ConfigReader[CNil] =
    ConfigReader.pure { (key, source) =>
      Left(ConfigError(
        s"Could not find any valid coproduct choice while reading ${source.keyType.name} [$key]"))
    }

  implicit def coproductConfigReader[A, B <: Coproduct](
    implicit readA: Lazy[ConfigReader[A]],
    readB: ConfigReader[B]
  ): ConfigReader[A :+: B] = {
    ConfigReader.pure { (key, source) =>
      readA.value.read(key)(source) match {
        case Right(a) => Right(Inl(a))
        case Left(aError) =>
          readB.read(key)(source) match {
            case Right(b) => Right(Inr(b))
            case Left(bError) => Left(aError combine bError)
          }
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
