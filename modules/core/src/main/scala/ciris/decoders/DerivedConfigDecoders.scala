package ciris.decoders

import cats.implicits._
import cats.Monad
import ciris._
import ciris.ConfigError

trait DerivedConfigDecoders {
  implicit def optionConfigDecoder[A, B](
    implicit decoder: ConfigDecoder[A, B]
  ): ConfigDecoder[A, Option[B]] =
    new ConfigDecoder[A, Option[B]] {
      override def decode[F[_]: Monad, K, S](
        entry: ConfigEntry[F, K, S, A]
      ): F[Either[ConfigError, Option[B]]] = {
        entry.value.flatMap {
          case Left(error) if error.isMissingKey =>
            ConfigError.right(Option.empty[B]).pure[F]
          case _ =>
            decoder.decode(entry).map(_.map(Some.apply))
        }
      }
    }
}
