package ciris.generic.decoders

import ciris.api._
import ciris.api.syntax._
import ciris.{ConfigError, ConfigDecoder, ConfigEntry}
import ciris.ConfigError.{left, right}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

trait GenericConfigDecoders {
  implicit def cNilConfigDecoder[A]: ConfigDecoder[A, CNil] =
    new ConfigDecoder[A, CNil] {
      override def decode[F[_]: Monad, K, S](
        entry: ConfigEntry[F, K, S, A]
      ): F[Either[ConfigError, CNil]] = {
        left[CNil](ConfigError {
          s"Could not find any valid coproduct choice while decoding ${entry.keyType.name} [${entry.key}]"
        }).pure[F]
      }
    }

  implicit def coproductConfigDecoder[A, B <: Coproduct, C](
    implicit decodeC: Lazy[ConfigDecoder[A, C]],
    decodeB: ConfigDecoder[A, B]
  ): ConfigDecoder[A, C :+: B] =
    new ConfigDecoder[A, C :+: B] {
      override def decode[F[_]: Monad, K, S](
        entry: ConfigEntry[F, K, S, A]
      ): F[Either[ConfigError, C :+: B]] = {
        decodeC.value.decode(entry).flatMap {
          case Right(a) => right[C :+: B](Inl(a)).pure[F]
          case Left(aError) =>
            decodeB.decode(entry).map {
              case Right(b)     => right[C :+: B](Inr(b))
              case Left(bError) => left(aError combine bError)
            }
        }
      }
    }

  implicit def hNilConfigDecoder[A]: ConfigDecoder[A, HNil] =
    new ConfigDecoder[A, HNil] {
      override def decode[F[_]: Monad, K, S](
        entry: ConfigEntry[F, K, S, A]
      ): F[Either[ConfigError, HNil]] = {
        right[HNil](HNil).pure[F]
      }
    }

  implicit def hListConfigDecoder[A, B, C <: HList](
    implicit decodeB: Lazy[ConfigDecoder[A, B]],
    decodeC: ConfigDecoder[A, C]
  ): ConfigDecoder[A, B :: C] =
    new ConfigDecoder[A, B :: C] {
      override def decode[F[_]: Monad, K, S](
        entry: ConfigEntry[F, K, S, A]
      ): F[Either[ConfigError, B :: C]] = {
        (decodeB.value.decode(entry) product decodeC.decode(entry)).map {
          case (Right(b), Right(c))         => Right(b :: c)
          case (Left(error1), Right(_))     => Left(error1)
          case (Right(_), Left(error2))     => Left(error2)
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
