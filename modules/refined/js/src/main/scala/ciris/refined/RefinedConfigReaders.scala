package ciris.refined

import ciris.ConfigReader
import eu.timepit.refined.api.{RefType, Validate}

trait RefinedConfigReaders {
  implicit def refTypeConfigReader[F[_, _], T, P](
    implicit reader: ConfigReader[T],
    refType: RefType[F],
    validate: Validate[T, P]
  ): ConfigReader[F[T, P]] = {
    reader.mapEither("RefType")(refType.refine[P].apply(_))
  }
}
