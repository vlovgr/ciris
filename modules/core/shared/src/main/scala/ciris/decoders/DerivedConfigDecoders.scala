package ciris.decoders

import ciris._
import ciris.api._
import ciris.api.syntax._
import ciris.ConfigError.right

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
            right(Option.empty[B]).pure[F]
          case _ =>
            decoder.decode(entry).map(_.right.map(Some.apply))
        }
      }
    }
}
