package ciris.refined.readers

import ciris.ConfigReader
import eu.timepit.refined.api.{RefType, Validate}

import scala.reflect.ClassTag

trait RefinedConfigReaders {
  implicit def refTypeConfigReader[F[_, _], T, P](
    implicit reader: ConfigReader[T],
    refType: RefType[F],
    validate: Validate[T, P],
    typeTag: ClassTag[T]
  ): ConfigReader[F[T, P]] = {
    val typeName = implicitly[ClassTag[T]].runtimeClass.getName
    reader.mapEither(typeName)(refType.refine[P].apply(_))
  }
}
