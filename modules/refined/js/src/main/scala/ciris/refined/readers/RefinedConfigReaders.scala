package ciris.refined.readers

import ciris.ConfigReader
import eu.timepit.refined.api.{RefType, Validate}

import scala.reflect.ClassTag

trait RefinedConfigReaders {
  implicit def refTypeConfigReader[F[_, _], T, P](
    implicit reader: ConfigReader[T],
    refType: RefType[F],
    validate: Validate[T, P],
    classTag: ClassTag[F[T, P]]
  ): ConfigReader[F[T, P]] = {
    val typeName = classTag.runtimeClass.getSimpleName
    reader.mapEither(typeName)(refType.refine[P].apply(_))
  }
}
